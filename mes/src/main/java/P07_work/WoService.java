package P07_work;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import P06_prod.DTO.PlanWoDTO;

import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class WoService {
	
	public Map getList(WoDTO dto) {
		Map map = new HashMap();
		WoDAO dao = new WoDAO();
		
		int size = dto.getSize();
		int page = dto.getPage();
		
		int start = 0, end = 0;
		
		end = size*page;
		start = end - (size-1);
		
		int cnt = dao.count();
		
		map.put("list", dao.getList(start, end));
		map.put("totalPage", (int)Math.ceil ((double)cnt/size));
		map.put("size", size);
		map.put("page", page);
		
		return map;
	}
	
	public Map search(WoDTO dto, SearchDTO search) {
		Map map = new HashMap();
		WoDAO dao = new WoDAO();
		
		int size = dto.getSize();
		int page = dto.getPage();
		
		int start = 0, end = 0;
		
		end = size*page;
		start = end - (size-1);
		
		int cnt = dao.countSearch(search);
		
		map.put("list", dao.search(start, end, search));
		map.put("totalPage", (int)Math.ceil ((double)cnt/size));
		map.put("size", size);
		map.put("page", page);
		
		return map;
	}
	
	public WoDTO detail(WoDTO dto) {
		WoDAO dao = new WoDAO();
		return dao.detail(dto);
	}
	
	public List planList () {
		WoDAO dao = new WoDAO();
		return dao.planList();
	}
	
	public PlanWoDTO getPlan(PlanWoDTO dto) {
		WoDAO dao = new WoDAO();
		return dao.getPlan(dto);
	}
	
	public List searchWorker(String keyword) {
	    WoDAO dao = new WoDAO();
	    return dao.searchWorker(keyword);
	}
	
	public int addOrder(WoAddDTO dto) {
		WoDAO dao = new WoDAO();
		return dao.addOrder(dto);
	}
	
	public int modifyOrder(WoAddDTO dto) {
		WoDAO dao = new WoDAO();
		return dao.modifyOrder(dto);
	}
	
	public int deleteOrder(String woId) {
		WoDAO dao = new WoDAO();
		return dao.deleteOrder(woId);
	}
	
	public int updateContent(String woId, int status, int prevQty) {
		WoDAO dao = new WoDAO();
		return dao.updateContent(woId, status, prevQty);
	}
	
	public int updatePlan() {
		WoDAO dao = new WoDAO();
		return dao.updatePlan();
	}
	
	public LotDTO getLot(String itemId) {
		WoDAO dao = new WoDAO();
		return dao.getLot(itemId);
	}

	public List<WoBOMDTO> setBOM(WoBOMDTO dto) {
	    WoDAO dao = new WoDAO();
	    return dao.setBOM(dto);
	}
	
	public List<ProcessDTO> setProcess(ProcessDTO dto) {
		WoDAO dao = new WoDAO();
		return dao.setProcess(dto);
	}
	
//	public int insertOut(Connection conn, LotDTO dto, String worker) {
//		WoDAO dao = new WoDAO();
//		return dao.insertOut(dto, worker);
//	}
//	
//	public int updateLot(Connection conn, LotDTO dto) {
//		WoDAO dao = new WoDAO();
//		return dao.updateLot(dto);
//	}
//	
//	public int insertIn(Connection conn, LotDTO dto, String worker) {
//		WoDAO dao = new WoDAO();
//		return dao.insertIn(dto, worker);
//	}
//	
//	public int minStock(Connection conn, String itemId, double qty) {
//		WoDAO dao = new WoDAO();
//		return dao.minStock(itemId, qty);
//	}

//	public List setBOM(Connection conn, WoBOMDTO dto) {
//		WoDAO dao = new WoDAO();
//		return dao.setBOM(dto);
//	}
//	
	
	
	/////////////////////////////////////////
	
	public void processContentModify(String woId, int status, int prevQty, String worker) {

	    Connection conn = null;

	    try {
	        Context ctx = new InitialContext();
	        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

	        conn = dataFactory.getConnection();
	        conn.setAutoCommit(false); // 트랜잭션 시작

	        WoDAO dao = new WoDAO();

	        // 1. 기존 작업 조회
	        WoDTO param = new WoDTO();
	        param.setWoId(woId);
	        
	        WoDTO old = dao.detail(conn, param);
	        
	        if (worker == null || worker.trim().isEmpty()) {
	            throw new RuntimeException("작업자 정보가 없습니다.");
	        }

	        if (old == null || old.getWoId() == null) {
	            throw new RuntimeException("작업지시 없음");
	        }
	        
	        if (status != 10 && status != 20 && status != 30 && status != 50) {
	            throw new RuntimeException("상태값 오류");
	        }
	        
	        if (prevQty < 0 || prevQty > old.getWoQty()) {
	            throw new RuntimeException("완료 수량 오류");
	        }

	        int oldStatus = old.getWoStatus();

	        // 2. 작업지시 업데이트
	        int updateResult = dao.updateContent(conn, woId, status, prevQty);
	        if (updateResult == 0) {
	            throw new RuntimeException("작업기록 수정에 실패했습니다.");
	        }

	        // 3. 완료로 바뀐 경우만 BOM 처리
	        if (oldStatus == 30 && status == 30) {
	            throw new RuntimeException("이미 완료된 작업은 수정할 수 없습니다.");
	        }
	        
	        if (oldStatus != 30 && status == 30) {
	            completeWorkByBom(conn, woId, prevQty, worker);
	        }

	        // 4. 계획 업데이트
	        dao.updatePlan(conn);

	        conn.commit(); // 성공 → 전부 반영

	    } catch (Exception e) {
	        try {
	            if (conn != null) conn.rollback(); // 실패 → 전부 취소
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        throw new RuntimeException(e);

	    } finally {
	        try {
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public void completeWorkByBom(Connection conn, String woId, int completedQty, String worker) {
        WoDAO dao = new WoDAO();

        WoBOMDTO param = new WoBOMDTO();
        param.setWoId(woId);

        List<WoBOMDTO> bomList = dao.setBOM(conn, param);

        if (bomList == null || bomList.isEmpty()) {
            throw new RuntimeException("BOM 정보가 없습니다.");
        }

        for (WoBOMDTO bom : bomList) {
            double requiredQty = bom.getEa() * completedQty;
            
            System.out.println("ea : " + bom.getEa());
            System.out.println("qty : " + completedQty);

            while (requiredQty > 0.000001) {
            	LotDTO lot = dao.getLot(conn, bom.getcId());

                // lot 없으면 바로 예외
                if (lot == null) {
                    throw new RuntimeException("사용 가능한 LOT이 부족합니다. itemId=" + bom.getcId());
                }

                double lotQty = lot.getQty();

                if (lotQty <= 0) {
                    throw new RuntimeException("LOT 수량이 0 이하입니다. lotId=" + lot.getLotId());
                }

                // 실제 이번 loop에서 소비할 양
                double consumeQty = Math.min(requiredQty, lotQty);

                // 1. stock 차감 - 전체 requiredQty가 아니라 실제 사용량만 차감
                dao.minStock(conn, bom.getcId(), consumeQty);

                // 2. 출고 로그 1건
                dao.insertOut(conn, lot, worker);

                // 3. lot 수량 갱신
                double remainQty = lotQty - consumeQty;
                lot.setQty(remainQty);

                if (remainQty == 0) {
                    lot.setStatus("사용 완료");
                } else {
                    lot.setStatus("사용 중");
                }

                dao.updateLot(conn, lot);

                // 4. 일부만 쓰고 남았으면 잔여 입고 로그 1건
                if (remainQty > 0) {
                	dao.insertIn(conn, lot, worker);
                }

                // 5. 남은 필요량 차감
                requiredQty -= consumeQty;
            }
        }
    }
	
	

	
	
	
	
	
}
