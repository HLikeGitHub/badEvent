package com.yjy.web.comm;

/**
 * @author rwx
 * @info desc
 * @email aba121mail@qq.com
 */
public class Main {

    public static void main(String[] args) throws Exception{

//        List<File> rpms = Arrays.asList(new File("/data/yum/media/Packages").listFiles());
//        List<String> files = new LinkedList<>();
//        for (File rpm : rpms) {
//            files.add(rpm.getPath());
//        }
//
//        File oldRpmFile = new File("/data/yum/media/Packages.t");
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(oldRpmFile)));
//        String line = null;
//        List<String> oldRpms = new ArrayList<>();
//        while ((line = bufferedReader.readLine()) != null){
//            oldRpms.add(line);
//        }
//        bufferedReader.close();
//
//        for (String oldRpm : oldRpms) {
//            String target = "/data/yum/media/Packages/"+oldRpm;
//            if(files.contains(target)){
//                System.out.println(target+"--->>>"+new File(target).delete());
//            }
//        }

//        String key = "www.bjchweb.v587";
////        String plainText = "{\"token\":\"1023311276055102\", \"healthCard\":\"20531000\"}";
//        String plainText = "{\"phone\": \"17788725721\",\"pwd\": \"123456\"}";
//        String cipherText = AESUtil.encrypt(plainText, key);
//        System.out.println(cipherText);
//        plainText = AESUtil.decrypt("zUubR2xh4q27QQPxk8wcVCQ0zTFyvQvoXHo1O0bh3LgNDCHBWZ0QW2lMSnWglWLNoGYeNdXCFVFwNDgvoEChGdlRG63YyJWbOehYXbV4VLdj7R9pNmV1H0T3C2mc9%2FKhzgigIQwMjIsFysKeGxOReWC8rPPXP%2F5OEOToodn4slj6xU7BXa5UFn3ofmhRIOzoVG9lbJlPQoP5qLvAqnOmmmQ7ql6SjResPVMzy4rFxdCm0uO01%2FG1qGz%2BnybsuEpK", key);
//        System.out.println(plainText);

//        String result = SenDetUtil.getInstance().isPornographic("123456789asdfl;jawseifjl;asdfj;asdf暴干a;sdfjwaifawelkfj;asdjf;asdfasdfasdf");
//        System.out.println(result);

//        String hash = HashUtil.md5("appid=wxb584042fd40a695e&attach=1分钱支付测试&body=APP-支付&mch_id=10016592&nonce_str=5K8264ILTKCH16CQ2502SI8ZNMTM67VS&notify_url=http://weixin.bjch.com/zxyydoa/app/wxpay/&openid=oyYi1s2CmQFZ5YrxQ52obUUfl358&out_trade_no=20191116161122000_123456789abcde&spbill_create_ip=192.168.1.168&total_fee=1&trade_type=JSAPI&key=44071119800620541744071119800620");
//        System.out.println(hash.toUpperCase());
//        System.out.println("9A0A8659F005D6984697E2CA0A9CF3B7".equalsIgnoreCase(hash));

//        System.out.println(USER_AGENT);


//        String certPath = "D:\\projects\\app\\local\\code\\server\\src\\main\\resources\\report.exe";
//        File file = new File(certPath);
//        InputStream certStream = new FileInputStream(file);
//        System.out.println(certStream.available());
//        System.out.println(file.length());
//        byte[] certData = new byte[(int) file.length()];
//        certStream.read(certData);
//        certStream.close();

//        WXPayConfigImp config = WXPayConfigImp.getInstance();
//        WXPay wxpay = new WXPay(config, false);
//
//        Map<String, String> data = new HashMap<String, String>();
//        data.put("body", "APP-支付测试");
//        data.put("attach", "1分钱支付测试");
//        data.put("notify_url", "http://weixin.bjch.com/web/app/app/wxpay/notify/");
//        data.put("openid", "oyYi1sxllStg62x1j4KT54Fd01hw");//oyYi1s2CmQFZ5YrxQ52obUUfl358
//        data.put("out_trade_no", "20191017113822000_12345678900010");
//        data.put("spbill_create_ip", "192.168.1.168");
//        data.put("total_fee", "1");
//        data.put("trade_type", "JSAPI");

//        Map<String, String> data = new HashMap<String, String>();
//        data.put("out_trade_no", "20191116161122000_123456789abcde");
//        data.put("out_trade_no", "20191017113822000_abc123456789de");
//        data.put("out_trade_no", "20191017113822000_abcde123456789");
//        data.put("out_trade_no", "20191017113822000_12345678900001");
//        data.put("out_trade_no", "20191017113822000_12345678900002");

        try {
//            Map<String, String> resp = pay.unifiedOrder(data);
//            Map<String, String> resp = pay.orderQuery(data);
//            Map<String, String> resp = pay.closeOrder(data);

//            System.out.println(resp);
//            System.out.println(WXPayUtil.isSignatureValid(resp, "44071119800620541744071119800620"));
//
//            Map<String, String> param = new HashMap<>();
//            param.put("appId", resp.get("appid"));
//            param.put("nonceStr", WXPayUtil.generateNonceStr());
//            param.put("package", "prepay_id="+resp.get("prepay_id"));
//            param.put("signType", WXPayConstants.MD5);
//            param.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");
//            param.put("paySign", WXPayUtil.generateSignature(param, "44071119800620541744071119800620"));
//            String json = JSONUtil.toJSON(param);
//            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
