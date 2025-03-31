package com.golmart.utils;

public class Constants {
    public static String REGEX_MAIL_PATTERN = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b";
    public static String REGEX_PHONE_PATTERN = "\"^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$\"";

    public interface MAIL_CONTENT {
        String SUBJECT_REGISTER_SUCCESS = "mail.register.subject";
        String BODY_REGISTER_SUCCESS = "mail.register.body";
        String SUBJECT_FORGOT_PASS_SUCCESS = "mail.forgot.subject";
        String BODY_FORGOT_PASS_SUCCESS = "mail.forgot.pass.body";
    }

    public interface MAIL_STATUS {
        String SUCCESS = "SUCCESS";
        String ERROR = "ERROR";
    }

    public interface PREFIX {
        String PACKAGE_PREFIX = "";
    }

    public interface STATUS {
        Integer ACTIVE = 1;
        Integer DELETE = 0;
    }

    public interface BANK_STATUS {
        Integer PENDING = 1;
        Integer ERROR = 0;
        Integer SUCCESS = 2;
    }

    public interface BANK_TYPE {
        Integer NAP_TIEN = 1;
        Integer RUT_TIEN = 0;
        Integer DAT_COC = 2;
        Integer TAT_TOAN = 3;
        Integer HOAN_TIEN = 4;
        Integer THANH_TOAN_DON_HANG = 5;
        Integer THANH_TOAN_VAN_DON = 6;
    }

    public interface ROLE {
        Long KHACH_HANG = 0L;
        Long ADMIN = 1L;
        Long NHAN_VIEN_TU_VAN = 2L;
        Long NHAN_VIEN_MUA_HANG = 3L;
        Long NHAN_VIEN_KHO = 4L;
    }
    public interface ORDER_SHIP_CODE_STATUS {
        Integer CREATE = 1;
        Integer APPROVE = 2;
        Integer REJECT = 3;
    }

    public interface COMPLAIN {
        Long WAIT = 1L;
        Long IN_PROCESS = 2L;
        Long SUCCESS = 3L;
        Long FAIL = 4L;
        Long ORDER_PROCESSING = 5L;
        Long REMOVE = 6L;
    }
    public interface purchaseServicePriceCode {
        String SERVICE_FEE = "SERVICE_FEE";
        String SERVICE_FEE_SALE = "SERVICE_FEE_SALE";
        String DOWN_PAYMENT = "DOWN_PAYMENT";
        String KDSP = "KDSP";
        String KDSP_1 = "KDSP_1";
    }

    public interface ORDER_ITEM_STATUS {
        Integer CART = 2;
        Integer ORDERED = 3;
        Integer DELETE = 0;
    }

    public interface OPTION_SET_CODE {
        String TY_GIA = "TY_GIA";
        String FEE_SHIP_WEIGHT = "FEE_SHIP_WEIGHT"; //Bảng giá vận chuyển hàng lẻ, kuadi, hàng order ( đơn thường )
        String FEE_SHIP_WEIGHT_KG = "DEPOSIT_SHIP_SINGLE"; //Bảng giá vận chuyển hàng lẻ, kuadi, hàng order ( đơn ký gửi)
        String WOOD_FEE = "WOOD_FEE"; //phí đóng gỗ
        String BULKY_GOODS = "BULKY_GOODS"; //Bảng giá vận chuyển hàng hóa cồng kềnh ( thể tích)
        String DEPOSIT_BULK_GOODS = "DEPOSIT_BULK_GOODS"; //Phí vận chuyển đối với hàng cồng kềnh ( tính theo khối  không quy đổi cân nặng) sử dụng cho đơn ký gửi
        String FIRST_WEIGHT = "0-50"; //CẤU HÌNH CÂN NẶNG MIN
        String LIEN_HE = "liên hệ";
    }
    public interface ORDER_STATUS {
        Integer DA_DUYET = 1;
        Integer DA_DAT_COC = 2;
        Integer DA_MUA_HANG = 3;
        Integer HANG_DA_VE_KHO_TQ = 4;
        Integer HANG_DA_VE_KHO_VN = 5;
        Integer SAN_SANG_GIAO_HANG = 6;
        Integer DA_GIAO = 7;
        Integer CHO_XU_LY_KHIEU_NAI = 8;
        Integer DA_KET_THUC = 9;
        Integer DA_HUY = 0;
    }
    public interface ORDER_TYPE {
        Integer THUONG = 1;
        Integer KY_GUI = 2;
        Integer TMDT = 3;
    }
    public interface PACKAGE_STATUS {
        Integer NB_PHAT_HANH = 1;
        Integer NHAP_KHO_TQ = 2;
        Integer GUI_BAO_TQ = 3;
        Integer NHAN_BAO_VIETNAM = 4;
        Integer DA_KIEM = 5;
        Integer NHAP_KHO_VN = 6;
        Integer DA_GIAO = 7;
        Integer SAN_SANG_GIAO_HANG = 8;
        Integer DETELE = 0;
    }
    public interface BAG_STATUS {
        Integer MO = 1;
        Integer GUI_HANG = 2;
        Integer NHAN_HANG = 3;
        Integer DONG = 0;
    }
    public interface DELIVELY_STATUS {
        Integer CHUA_GIAO = 1;
        Integer DA_GIAO = 2;
    }
    public enum ORDER_QUERY_CRITERIA {
        WITHOUT_SHOP("WITHOUT_SHOP"),
        WITHOUT_DOMESTIC_FEE("WITHOUT_DOMESTIC_FEE"),
        WITHOUT_SHIP_CODE("WITHOUT_SHIP_CODE");

        private final String value;

        ORDER_QUERY_CRITERIA(String value) {
            this.value = value;
        }

        public static ORDER_QUERY_CRITERIA fromString(String value) {
            for (ORDER_QUERY_CRITERIA criterion : ORDER_QUERY_CRITERIA.values()) {
                if (criterion.value.equals(value)) {
                    return criterion;
                }
            }
            return null;
        }

        public String getValue() {
            return this.value;
        }
    }
    public interface CODE {
        String ORDER = "CN_";
        String ORDER_TMDT = "SK_";
        String ORDER_KY_GUI = "KG_";
    }

    public interface SHOP {
        String SHOP_TAOBAO = "taobao";
        String SHOP_1688 = "1688";
        String SHOP_TMALL = "tmall";
    }

    public static Integer getStatusOrderByPackage(Integer packageStatus){
        if(PACKAGE_STATUS.NB_PHAT_HANH.equals(packageStatus)){
            return ORDER_STATUS.DA_MUA_HANG;
        }
        if(PACKAGE_STATUS.NHAP_KHO_TQ.equals(packageStatus) || PACKAGE_STATUS.GUI_BAO_TQ.equals(packageStatus)){
            return ORDER_STATUS.HANG_DA_VE_KHO_TQ;
        }
        if(PACKAGE_STATUS.NHAN_BAO_VIETNAM.equals(packageStatus) || PACKAGE_STATUS.NHAP_KHO_VN.equals(packageStatus)){
            return ORDER_STATUS.HANG_DA_VE_KHO_VN;
        }
        if(PACKAGE_STATUS.DA_KIEM.equals(packageStatus) || PACKAGE_STATUS.SAN_SANG_GIAO_HANG.equals(packageStatus)){
            return ORDER_STATUS.SAN_SANG_GIAO_HANG;
        }
        if (PACKAGE_STATUS.DA_GIAO.equals(packageStatus)){
            return ORDER_STATUS.DA_GIAO;
        }
        return 0;
    }

    public static class NotiStatus {
        public static final String NEW = "0";
        public static final String SEEN = "1";
    }

    public static class NotiType {
        public static final String DON_HANG = "1";
        public static final String VAN_DON = "2";
        public static final String VI_DIEN_TU = "3";
    }
    public static String DATE_TIME_FORMAT_24H = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_TIME_FORMAT_12H = "yyyy-MM-dd hh:mm:ss";

}
