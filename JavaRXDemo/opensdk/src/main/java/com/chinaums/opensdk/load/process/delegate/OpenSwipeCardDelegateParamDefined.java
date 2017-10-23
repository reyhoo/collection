package com.chinaums.opensdk.load.process.delegate;


public class OpenSwipeCardDelegateParamDefined {

    public static class OpenSwipeCardRequest {

    }

    public static class OpenSwipeCardResponse {

        /**
         * resultCode
         */
        public int resultCode = Integer.MAX_VALUE;

        /**
         * 前6后4的卡号（都会返回）
         */
        public String cardNumber;

        /**
         * 盒子号（都会返回）
         */
        public String boxSid;

        /**
         * 磁道号的ksn（mpos会返回）
         */
        public String trackKsn;

        /**
         * mpos磁道加密数据（mpos会返回）
         */
        public String track2DataKsn;

        /**
         * track2：一盒宝加密的磁道信息（一盒宝会返回）
         */
        public String track2;

    }


}
