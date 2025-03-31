//package com.golmart.Controller;
//
//import com.app.my_app.entities.Staff;
//import com.app.my_app.model.ResponseError;
//import com.app.my_app.repos.StaffRepository;
//import com.app.my_app.service.StatisticsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping(value = "/api/statis", produces = MediaType.APPLICATION_JSON_VALUE)
//public class StatisticsController {
//    @Autowired
//    private StatisticsService statisticsService;
//    @Autowired
//    private StaffRepository staffRepository;
//    @GetMapping("/statistics-negotiable-spreads")
//    public ResponseEntity<?> statisticsNegotiableSpreads( @RequestParam(value = "staffId",required = false) String staffName,
//                                            @RequestParam(value = "fromDate",required = false) String fromDate,
//                                          @RequestParam(value = "toDate",required = false) String toDate) {
//        try{
//            List<Long> staffIds = null;
//            if (!ObjectUtils.isEmpty(staffName)){
//                List<Staff> staffs =  staffRepository.findStaffByUsername(staffName);
//                if (!ObjectUtils.isEmpty(staffs)){
//                    staffIds = staffs.stream().map(Staff::getId).collect(Collectors.toList());
//                }
//            }
//            return ResponseEntity.ok(statisticsService.statisticsNegotiableSpreads(fromDate,toDate,staffIds));
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.builder().message(e.getMessage()).build());
//        }
//    }
//
//    @GetMapping("/sales-statistics")
//    public ResponseEntity<?> salesStatistics(@RequestParam(value = "fromDate",required = false) String fromDate,
//                                    @RequestParam(value = "toDate",required = false) String toDate) {
//        try{
//            return ResponseEntity.ok(statisticsService.salesStatistics(fromDate, toDate));
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.builder().message(e.getMessage()).build());
//        }
//    }
//
//    @GetMapping("/full-order-statistics")
//    public ResponseEntity<?> fullOrderStatistics(@RequestParam(value = "fromDate",required = false) String fromDate,
//                                    @RequestParam(value = "toDate",required = false) String toDate) {
//        try{
//            return ResponseEntity.ok(statisticsService.fullOrderStatistics(fromDate,toDate));
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.builder().message(e.getMessage()).build());
//        }
//    }
//}
