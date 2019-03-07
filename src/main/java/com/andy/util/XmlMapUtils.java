package com.andy.util;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.alibaba.fastjson.JSONObject;
import com.andy.domain.Goods;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.util.StringUtils;


/**
 * 提供Map<String,Object>转XML，XML转Map<String,Object>
 * 参考文章：https://blog.csdn.net/qq_40150691/article/details/80813459
 * @author MOSHUNWEI
 * @version 5.0
 * @since 2018-03-09
 */


public class XmlMapUtils {


    /**
     * 通过Map创建XML,Map可以多层转换
     *
     * @param params
     * @return String-->XML
     */
    public static String createXmlByMap(String parentName, Map<String, Object> params, boolean isCDATA) {
        Document doc = DocumentHelper.createDocument();
        doc.addElement(parentName);
        String xml = iteratorXml(doc.getRootElement(), parentName, params, isCDATA);
        return formatXML(xml);
    }

    /**
     * 通过Map创建XML,Map可以多层转换
     * 可以自定义parent节点
     *
     * @param params
     * @return String-->XML
     */
    public static String createXmlByMap(String parentName, Map<String, Object> params) {
        Document doc = DocumentHelper.createDocument();
        doc.addElement(parentName);
        String xml = iteratorXml(doc.getRootElement(), parentName, params, false);
        return formatXML(xml);
    }

    /**
     * 通过Map创建XML,Map可以多层转
     * 固定节点parent为Document
     *
     * @param params
     * @return String-->XML
     */
    public static String createXmlByMap(Map<String, Object> params) {
        String parentName = "Document";
        Document doc = DocumentHelper.createDocument();
        doc.addElement(parentName);
        String xml = iteratorXml(doc.getRootElement(), parentName, params, false);
        return formatXML(xml);
    }

    /**
     * MapToXml循环遍历创建xml节点
     * 此方法在value中加入CDATA标识符
     *
     * @param element    根节点
     * @param parentName 子节点名字
     * @param params     map数据
     * @return String-->Xml
     */

    @SuppressWarnings("unchecked")
    public static String iteratorXml(Element element, String parentName, Map<String, Object> params, boolean isCDATA) {
        Element e = element.addElement(parentName);
        Set<String> set = params.keySet();
        for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            if (params.get(key) instanceof Map) {
                iteratorXml(e, key, (Map<String, Object>) params.get(key), isCDATA);
            } else {
                String value = params.get(key) == null ? "" : params.get(key).toString();
                if (!isCDATA) {
                    e.addElement(key).addText(value);
                } else {
                    e.addElement(key).addCDATA(value);
                }
            }
        }
        return e.asXML();
    }

    /**
     * 格式化xml,显示为容易看的XML格式
     *
     * @param inputXML
     * @return
     */
    public static String formatXML(String inputXML) {
        String requestXML = null;
        XMLWriter writer = null;
        Document document = null;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(new StringReader(inputXML));
            if (document != null) {
                StringWriter stringWriter = new StringWriter();
                OutputFormat format = new OutputFormat("	", true);//格式化，每一级前的空格
                format.setNewLineAfterDeclaration(false);    //xml声明与内容是否添加空行
                format.setSuppressDeclaration(false);        //是否设置xml声明头部
                format.setNewlines(true);        //设置分行
                writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
                requestXML = stringWriter.getBuffer().toString();
            }
            return requestXML;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {

                }
            }
        }
    }


    /**
     * 通过XML转换为Map<String,Object>
     *
     * @param xml 为String类型的Xml
     * @return 第一个为Root节点，Root节点之后为Root的元素，如果为多层，可以通过key获取下一层Map
     */
    public static Map<String, Object> createMapByXml(String xml) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (doc == null)
            return map;
        Element rootElement = doc.getRootElement();
        elementTomap(rootElement, map);
        return map;
    }

    /***
     *
     * XmlToMap核心方法，里面有递归调用
     *
     * @param map
     * @param ele
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> elementTomap(Element outele, Map<String, Object> outmap) {
        List<Element> list = outele.elements();
        int size = list.size();
        if (size == 0) {
            outmap.put(outele.getName(), outele.getTextTrim());
        } else {
            Map<String, Object> innermap = new HashMap<String, Object>();
            for (Element ele1 : list) {
                String eleName = ele1.getName();
                Object obj = innermap.get(eleName);
                if (obj == null) {
                    elementTomap(ele1, innermap);
                } else {
                    if (obj instanceof java.util.Map) {
                        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                        list1.add((Map<String, Object>) innermap.remove(eleName));
                        elementTomap(ele1, innermap);
                        list1.add((Map<String, Object>) innermap.remove(eleName));
                        innermap.put(eleName, list1);
                    } else {
                        elementTomap(ele1, innermap);
                        ((List<Map<String, Object>>) obj).add(innermap);
                    }
                }
            }
            outmap.put(outele.getName(), innermap);
        }
        return outmap;
    }

    public static void main(String[] args) {
        String s = "{\"state\":{\"code\":\"1000\",\"message\":\"成功\",\"solution\":\"接口访问成功\"},\"totalCount\":\"1\",\"productList\":[{\"packageId\":7330,\"packageName\":\"<![CDATA[套餐A（往返交通+住宿）+A行程]]>\",\"packageStatus\":\"<![CDATA[false]]>\",\"isPackage\":\"<![CDATA[true]]>\",\"adultNum\":2,\"childNum\":0,\"roomMax\":\"2\",\"min\":1,\"max\":10,\"product\":{\"productId\":263211,\"productName\":\"<![CDATA[住1晚普陀山（普陀山庄＋双人早餐）＋上海-舟山往返巴士车票2张]]>\",\"productAct\":\"<![CDATA[海滨岛屿,宗教祈福]]>\",\"productStatus\":\"<![CDATA[false]]>\",\"productClass\":17,\"productType\":\"<![CDATA[INNERLINE]]>\",\"day\":\"<![CDATA[2]]>\",\"night\":\"<![CDATA[1]]>\",\"otherServe\":\"<![CDATA[小交通,酒店,门票,餐券,]]>\",\"address\":\"<![CDATA[舟山 普陀区 东港海印路850号普陀·杉杉广场A3座]]>\",\"placeArriveId\":\"<![CDATA[96,114,200708,3934,103940]]>\",\"placeArrive\":\"<![CDATA[浙江,舟山,普陀区,普陀山,朱家尖风景区]]>\",\"recommend\":\"<![CDATA[海天佛国，渔都港城，闹中取静！\\r\\n别具一格的舟山泊宁酒店，是您在忙碌一天后憩息最好的选择！]]>\",\"productInfo\":\"<![CDATA[<div style=\\\"margin-bottom: 15px;padding-bottom: 10px;border-bottom: 1px solid #E3E3E3;\\\"><div style=\\\"font-size: 20px;color: #333;line-height: 20px;\\\"><span style=\\\"width: 88px;float: left;font-size: 20px;color: #333;line-height: 20px;border-right: 1px solid #E3E3E3;\\\">酒店介绍</span><span style=\\\"display: inline-block;height: 20px;width: 200px;background: url(http://pic.lvmama.com/img/line/product-detail-all.png)   0 -630px;\\\"></span></div></div><div style=\\\"font-size: 16px;color: #333;line-height: 24px;margin-bottom: 6px;\\\">舟山泊宁酒店(舟山杉杉普陀天地店)</div><div style=\\\"font-size: 14px;line-height: 22px;color: #666;margin-bottom: 10px;\\\">　泊宁酒店(舟山杉杉普陀天地店)位于东港内湖商业圈-普陀天地·杉杉广场内，周边配套设施完善。与海天佛国“普陀山”隔海相望，站在酒店海景观景阳台举目远眺可以看到威严耸立的普陀山“观音大佛”。<br/>　　酒店全LOFT客房，由杉杉集团和高远文旅集团全力精心打造，按照现代化风格装修，客房布置温馨，选用国际品牌“丝涟”床垫，打造一流睡眠环境。“泊宁”目前国内有三十余家门店，秉承“待人如己，礼众利他”的经 营理念为你的旅途中营造一份家的温暖和关爱。<br/></div><div style=\\\"font-size: 0; margin-bottom: 20px;\\\"><img style=\\\"width: 100%;\\\" src=\\\"http://pic.lvmama.com/uploads/pc/place2/2018-06-23/6a1cd62e-df95-4a06-9b6d-d1c6f4ffbbe1.jpg\\\" alt=\\\"\\\" width=\\\"878\\\" ></div><div style=\\\"margin-bottom: 15px;padding-bottom: 10px;border-bottom: 1px solid #E3E3E3;\\\"><div style=\\\"font-size: 20px;color: #333;line-height: 20px;\\\"><span style=\\\"width: 88px;float: left;font-size: 20px;color: #333;line-height: 20px;border-right: 1px solid #E3E3E3;\\\">景点介绍</span><span style=\\\"display: inline-block;height: 20px;width: 200px;background: url(http://pic.lvmama.com/img/line/product-detail-all.png)   0 -669px;\\\"></span></div></div><div style=\\\"font-size: 16px;color: #333;line-height: 24px;margin-bottom: 6px;\\\">普陀山风景区</div><div style=\\\"font-size: 14px;line-height: 22px;color: #666;margin-bottom: 10px;\\\">普陀山，国家5A级旅游景区，国家首批重点风景名胜区，全国文明风景旅游区示范点，全国保护旅游消费者权益示范单位，ISO14000国家示范区，全球优秀生态旅游景区，中国佛教四大名山之一。<br/>普陀山位于浙江杭州湾以东莲花洋，钱塘江口、舟山群岛东南部海域，景区包括普陀山、洛迦山、朱家尖，总面积41.95平方公里，其中普陀山本岛12.5平方公里，最高峰佛顶山海拔292米。普陀山既有悠久的佛教文化，又有丰富的海岛风光，古人称之为“海天佛国”、“南海圣境”、“人间第一清静境”。<br/>普陀山大海怀抱，金沙绵亘，景色优美，气候宜人。著名景点如潮音洞、梵音洞、朝阳洞、磐陀石、二龟听法石、百步沙、千步沙、普济寺、法雨寺、慧济寺、南海观音、大乘庵等。内外交通便捷，旅游环境良好。每年农历二月十九、六月十九、九月十九是观音香会、朝圣盛典，海内外香游客摩肩接踵、蜂拥而之。“海上有仙山，山在虚无缥缈间”，普陀山以其神奇、神圣、神秘，成为驰誉中外的旅游胜地。<br/></div><div style=\\\"margin-bottom: 15px;padding-bottom: 10px;border-bottom: 1px solid #E3E3E3;\\\"><div style=\\\"font-size: 20px;color: #333;line-height: 20px;\\\"><span style=\\\"width: 88px;float: left;font-size: 20px;color: #333;line-height: 20px;border-right: 1px solid #E3E3E3;\\\">交通信息</span><span style=\\\"display: inline-block;height: 20px;width: 200px;background: url(http://pic.lvmama.com/img/line/product-detail-all.png)   0 -750px;\\\"></span></div></div><div style=\\\"font-size: 0; margin-bottom: 20px;\\\"><img style=\\\"width: 100%;\\\" src=\\\"http://pic.lvmama.com/uploads/pc/place2/2018-07-02/c452f863-8de5-42db-a8d3-9b94ea6be655.jpg\\\" alt=\\\"\\\" width=\\\"878\\\" ></div><div style=\\\"font-size: 0; margin-bottom: 20px;\\\"><img style=\\\"width: 100%;\\\" src=\\\"http://pic.lvmama.com/uploads/pc/place2/2018-07-02/9301c80a-6241-4c1d-8400-849e0e2ed59c.jpg\\\" alt=\\\"\\\" width=\\\"878\\\" ></div><div style=\\\"font-size: 16px;color: #333;line-height: 24px;margin-bottom: 6px;\\\">朱家尖白山风景区（凭手机短信订单号至白山景区售票窗口取票入园）</div><div style=\\\"font-size: 14px;line-height: 22px;color: #666;margin-bottom: 10px;\\\">位于浙江舟山市朱家尖岛北部。山峦起伏，石景满山，由孤岩危立，或奇石叠耸。有的方整平坦，如刀削斧劈；有的突兀无规，若悬空欲坠；有的象窥视欲跃之青蛙，有的似引颈报晓的金鸡；有的如顶球玩耍的海狮，有的若亭亭玉立的少女；有的象水牛，似松鼠，如猴子……千姿万状，令人叹绝。<br/>白山石群全系花岗岩的球状风化和垂直裂隙，形成奇岩、怪石和奇洞异缝。石质呈灰白色，山也由此得名。山上林木葱茏，时有烟云过境，白石与绿树相映，翠岚与碧天相拥，于是有“海上雁荡”和“白山灵石”之誉。<br/></div><div style=\\\"font-size: 0; margin-bottom: 20px;\\\"><img style=\\\"width: 100%;\\\" src=\\\"http://pic.lvmama.com/uploads/pc/place2/2018-06-23/f45ff985-e631-49ac-abbb-dac66c9a4b1e.jpg\\\" alt=\\\"\\\" width=\\\"878\\\" ></div><div style=\\\"font-size: 0; margin-bottom: 20px;\\\"><img style=\\\"width: 100%;\\\" src=\\\"http://pic.lvmama.com/uploads/pc/place2/2018-06-23/270b3d40-a28c-49bb-80a7-64ad352b7fb9.jpg\\\" alt=\\\"\\\" width=\\\"878\\\" ></div><div style=\\\"font-size: 14px;line-height: 22px;color: #666;margin-bottom: 10px;\\\">以上信息仅供参考！<br/></div>]]>\",\"productTraffic\":\"<![CDATA[自驾信息：<br />\\r\\n1、上海出发→ 进入沪杭高速公路(行驶约62.7公里)→ 进入跨海大桥北接线(行驶约24.6公里)→ 下出口进入G329国道(过朱家尖跨海大桥，行驶约54.4公里)→ 到达舟山朱家尖。<br />\\r\\n2、 宁波出发→出发宁波进入609县道向慈海南路/329国道方向(行驶约3.9公里)左转进入慈海南路/329国道(行驶约1.3公里)在镇骆西路向右转 (行驶约6公里)→进入庄合线/602县道沿庄合线前行蛟川收费站上宁波连接线进入舟山跨海大桥进入→G329国道(过朱家尖跨海大桥，行驶约54.4公 里)→到达舟山朱家尖。<br />\\r\\n3、温州出发→ 进入甬台温高速公路(行驶约253公里)→ 进入世纪大道(行驶约6.5公里)→ 下出口进入G329国道(过朱家尖跨海大桥，行驶约54.4公里)→ 到达舟山朱家尖。<br />\\r\\n4、 杭州出发→进入杭甬高速公路(行驶约128公里)下宁波绕城/姜山/跨海大桥/镇海/绕城枢纽A 出口，进入宁波绕城高速(行驶约12.3公里)下保国寺/镇海出口→ 舟山跨海大桥下出口，进入G329高速公路(过朱家尖跨海大桥，行驶约54.4公里)到达舟山朱家尖。<br />\\r\\n以上信息仅供参考！<br />]]>\",\"feeInclude\":\"<![CDATA[套餐A\\r\\n【景点】2张成人普陀山风景区大门票；\\r\\n【美食】2份自助早餐；\\r\\n【住宿】舟山泊宁酒店1晚（标间和大床不能指定，敬请谅解！）。\\r\\n套餐B\\r\\n【交通】2张成人杭州—舟山往返二日旅游空调巴士票2张；\\r\\n【美食】2份自助早餐；\\r\\n【住宿】舟山泊宁酒店1晚（标间和大床不能指定，敬请谅解！）。\\r\\n套餐C\\r\\n【交通】2张成人上海—舟山往返二日旅游空调巴士票；\\r\\n【美食】2份自助早餐；\\r\\n【住宿】舟山泊宁酒店1晚（标间和大床不能指定，敬请谅解！）。\\r\\n套餐D\\r\\n【住宿】舟山泊宁酒店1晚（标间和大床不能指定，敬请谅解！）。\\r\\n【美食】2份自助早餐；\\r\\n【景点】2张成人朱家尖白山风景区门票；\\r\\n【特别说明】旅游大巴必须一人一座，若您选择的套餐包含巴士票，且您携带小孩请务必联系客服补车位差价，否则造成当天无法上车等损失，需要自行承担！\\r\\n【温馨提示】此套餐为双人套餐，若出现单人，请提前咨询【0580-6094243】\\r\\n]]>\",\"feeExclude\":\"<![CDATA[如需杭州市区内提供多地接车服务，萧山人民广场(金城路心中路口） /萧山江寺公园（萧然南路158号） /黄龙杭州旅游集散中心（西湖区黄龙路1号） 杭州大厦B座（下城区武林广场1号）（则需另付10元/人）？，下单烦请备注准确地址，如未备注则默认上车点，吴山路70号中环精品酒店门口自行上车\\r\\n\\r\\n除费用包含项目明细外，其他所有费用敬请自理。]]>\",\"warning\":\"<![CDATA[本产品由上海驴妈妈兴旅国际旅行社有限公司及具有合法资质的地接社提供相关服务。【重要提示】：即日起朱家尖——普陀山，半升洞——普陀山航线，游客购买船票已实行实名制；烦请各位游客携带好身份证噢！1.2m以下无需任何证件，1.2m-1.5m请提供及携带身份证、学生证、户口本等有效证件。若遇证件不符或证件错误需要自行承担损失了噢！<br />\\r\\n<br />\\r\\n一、酒店信息<br />\\r\\n【舟山泊宁酒店】<br />\\r\\n1、入住时间：为14:00后；<br />\\r\\n2、离店时间：为次日12:00前；<br />\\r\\n3、入住凭证：凭预订人手机号及个人有效身份证办理入住；<br />\\r\\n4、酒店地址：舟山 普陀区 东港海印路850号普陀·杉杉广场A3座（近朱家尖跨海大桥、凯虹广场、东港海鲜排挡、普陀天地）；<br />\\r\\n5、酒店电话：【0580-6206789】。<br />\\r\\n<br />\\r\\n<p>\\r\\n\\t<span style=\\\"color:#666666;\\\">二、门票信息</span> \\r\\n</p>\\r\\n<p>\\r\\n\\t<span style=\\\"color:#666666;\\\">【普陀山风景区门票</span><span style=\\\"color:#666666;\\\">】</span> \\r\\n</p>\\r\\n<p font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\" style=\\\"white-space: normal; color: rgb(102, 102, 102);\\\"><span font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\">1、入园方式：普陀山检票口刷二维码入园或刷身份证入园，提前一天会收到二维码短信（此门票为电子票，无需取票，可直接入园）。</span><br />\\r\\n<span font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\">（门票信息，出行前一天会有工作人员电话联系，请您保持电话畅通，谢谢配合！）</span><br />\\r\\n<span font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\">2、普陀山景区门票不包含寺院香花券和洛迦山景点门票；普陀山大门票仅包含景区大门票，不含各寺院香火券；普陀山门票1月和12月为淡季，门市价120 元；2月-11月为旺季，门市价160元；节假日（正月初一至初五、五月一日至三日、十月一日至五日），门市价180元。普陀山门票以景区门市价为准，如有变动请补足差价。普陀山门票即发票，故驴妈妈不再向游客开具发票。</span><br />\\r\\n<span font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\">3、免费政策：6周岁（含）以下或身高1.2m（含1.2m）以下的儿童免景区大门票；全国现役军人（含武警）、记者（持新闻总署颁发的记者证）、僧尼、全国劳动模范、全国道德模范残疾人、离休干部、革命伤残军人、烈士家属凭本人有效证件免景区大门票；全国70周岁以上（包括70周岁）的老年人凭本人身份证免景区大门票；全国的教师在教师节当天凭有效证件免景区大门票。</span><br />\\r\\n<span font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\">4、优惠政策：身高1.2m（不含）以上—1.5m（含）的儿童购景区优惠大门票；6周岁（不含）-18周岁（含）全国全日制普通学校的在校学生（不包括夜大、电大、网络教育、远程教育、成人教育、继续教育、脱产教育等）凭本人有效证件购景区优惠大门票；全国60周岁（含）至69周岁（含）的老年人凭本人身份证购景区优惠大门票。</span><br />\\r\\n<span font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\">5、普陀山景区接待时间：06:30-22:00，2次进山需在普陀山景点售票处签证一下。</span><br />\\r\\n<span font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\">以上信息仅供参考，具体信息请以普陀山景区当天披露为准。</span> \\r\\n\\t</p>\\r\\n<p font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\" style=\\\"white-space: normal; color: rgb(102, 102, 102);\\\"><span>【白山景区】</span><br />\\r\\n<span>1、入园时间：07:30-17:30；</span><br />\\r\\n<span>2、入园方式：凭预订人手机号码收到的短信二维码入园；</span><br />\\r\\n<span>3、有效期限：（有效期内可入园1次）指定游玩日当天内有效。</span><br />\\r\\n<span>以上信息仅供参考，具体信息请以普陀山景区当天披露为准。</span><br />\\r\\n<span>以上紧急联系方式：【13732523519】。</span> \\r\\n</p>\\r\\n<p font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\" style=\\\"white-space: normal; color: rgb(102, 102, 102);\\\"><br />\\r\\n\\t</p>\\r\\n<p font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\" style=\\\"white-space: normal; color: rgb(102, 102, 102);\\\">三、交通信息\\r\\n</p>\\r\\n<p font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\" style=\\\"white-space: normal; color: rgb(102, 102, 102);\\\">【上海人民广场——朱家尖码头】\\r\\n\\t</p>\\r\\n\\t<p>\\r\\n\\t\\t1、集合时间：06:40；\\r\\n\\t</p>\\r\\n\\t<p>\\r\\n\\t\\t2、发车时间：07:00；\\r\\n\\t</p>\\r\\n\\t<p>\\r\\n\\t\\t3、上车地点：上海市黄浦区人民广场（南京西路108号）金门大酒店门口；\\r\\n\\t</p>\\r\\n\\t<p>\\r\\n\\t\\t4、下车地点：浙江省舟山市普陀区朱家尖蜈蚣峙码头停车场；\\r\\n\\t</p>\\r\\n\\t<p>\\r\\n\\t\\t5、回程发车时间：13:30；\\r\\n\\t</p>\\r\\n\\t<p>\\r\\n\\t\\t6、回程上车地点：浙江省舟山市普陀区朱家尖蜈蚣峙码头停车场（即原下车地点）；\\r\\n\\t</p>\\r\\n\\t<p>\\r\\n\\t\\t7、回程下车地点：上海南站/上海人民广场。\\r\\n\\t</p>\\r\\n\\t<p>\\r\\n\\t\\t<br />\\r\\n\\t</p>\\r\\n\\t<p>\\r\\n\\t\\t<span style=\\\"color:#666666;\\\">四、交通信息</span> \\r\\n\\t</p>\\r\\n<p font-size:14px;background-color:#f5f5f5;\\\"=\\\"\\\" style=\\\"white-space: normal; color: rgb(102, 102, 102);\\\">【杭州吴山路70号中环精品酒店门口——朱家尖码头】\\r\\n</p>\\r\\n<p>\\r\\n\\t1、集合时间：07:00；\\r\\n</p>\\r\\n<p>\\r\\n\\t2、发车时间：07:20；\\r\\n</p>\\r\\n<p>\\r\\n\\t3、上车地点：杭州吴山路70号中环精品酒店门口；\\r\\n</p>\\r\\n<p>\\r\\n\\t4、下车地点：浙江省舟山市普陀区朱家尖蜈蚣峙码头停车场；\\r\\n</p>\\r\\n<p>\\r\\n\\t5、回程发车时间：13:30；\\r\\n</p>\\r\\n<p>\\r\\n\\t6、回程上车地点：浙江省舟山市普陀区朱家尖蜈蚣峙码头停车场（即原下车地点）；\\r\\n</p>\\r\\n<p>\\r\\n\\t7、回程下车地点：杭州解百新世纪商厦（距离西湖风景区800米左右）。\\r\\n</p>\\r\\n<p>\\r\\n\\t07:00杭州吴山路70号中环精品酒店门口（默认上车地）集合上车；如需 杭州市区内【萧山人民广场/萧山江寺公园/黄龙体育馆旅游集散中心/杭州大厦B座/各大酒店】提供接车服务需要另付10元/人，如需此地上车下单烦请备注！！！\\r\\n</p>\\r\\n<p>\\r\\n\\t以上如遇问题烦请拨打【13732523519】\\r\\n</p>\\r\\n<div>\\r\\n\\t<br />\\r\\n</div>\\r\\n<br />\\r\\n<br />]]>\",\"changeInfo\":\"<![CDATA[该产品不可退改]]>\",\"trafficInfo\":{},\"productImage\":[\"<![CDATA[http://pic.lvmama.com//uploads/pc/place2/2018-06-23/c0c212f5-1997-4fdd-a49c-f96b1539a6e3.jpg]]>\",\"<![CDATA[http://pic.lvmama.com//uploads/pc/place2/2018-06-23/83ff4a61-a90c-4f68-9e63-4bff0b308b43.jpg]]>\",\"<![CDATA[http://pic.lvmama.com//uploads/pc/place2/2018-06-23/7df7c5bb-ca1c-4858-b9a7-8501a9d74c8b.jpg]]>\",\"<![CDATA[http://pic.lvmama.com//uploads/pc/place2/2018-06-23/d2976792-85f1-4a66-834a-64f216136a0d.jpg]]>\",\"<![CDATA[http://pic.lvmama.com//uploads/pc/place2/2018-06-23/f8073418-435d-4a3d-96da-aa9aaa299f2c.jpg]]>\",\"<![CDATA[http://pic.lvmama.com//uploads/pc/place2/2018-06-23/21b12dff-5bee-4d0d-8429-e1a612e44d46.jpg]]>\",\"<![CDATA[http://pic.lvmama.com//uploads/pc/place2/2016-03-28/355e761e-d490-47c9-8bc3-9484547e4245.jpg]]>\",\"<![CDATA[http://pic.lvmama.com//uploads/pc/place2/2016-12-09/5e8d148e-6911-4c11-8e62-6ddea68f11cf.jpg]]>\",\"<![CDATA[http://pic.lvmama.com//uploads/pc/place2/2016-03-28/c3a70151-0519-4770-9d23-9b03d54317bb.jpg]]>\",\"<![CDATA[http://pic.lvmama.com//uploads/pc/place2/2016-12-09/8ed2a76f-3a6d-4fba-957d-d33154dd66b0.jpg]]>\"],\"trips\":[{\"tripDay\":1,\"tripTitle\":\"<![CDATA[全国各地---舟山]]>\",\"tripInfo\":\"<![CDATA[套餐A：自行前往舟山；\\r\\n套餐B：上车地点：杭州吴山路中环精品酒店门口\\r\\n             发车时间：07:00集合     07:20准时发车。（准时发车，过时不候，谢谢您的配合！）\\r\\n             下车地点：朱家尖蜈蚣峙码头停车场；\\r\\n【温馨提示】杭州市区内如需接车服务，下单烦请备注准确地址，如未备注。则默认吴山路70号中环精品酒店门口上车！\\r\\n【温馨提示】可乘坐地铁1号线至龙翔桥站下，D2出口，马路对面，沿吴山路直线步行200米，即可到达上车点\\r\\n接车点：\\r\\n ---06:00左右\\r\\n             ---萧山人民广场(金城路心中路口） \\r\\n             ---萧山江寺公园（萧然南路158号） \\r\\n----06：30左右\\r\\n            ---黄龙杭州旅游集散中心（西湖区黄龙路1号） \\r\\n            ---杭州大厦B座（下城区武林广场1号）\\r\\n下车点：---吴山路中环精品酒店门口统一集合发车前往舟山，07：20准时发车\\r\\n            到达时间：11:30左右。（仅供参考！）\\r\\n套餐C：上车地点：上海黄浦区人民广场（南京西路108号）金门大酒店门口；\\r\\n             发车时间：07:00。（准时发车，过时不候，谢谢您的配合！）\\r\\n             下车地点：朱家尖蜈蚣峙码头停车场；\\r\\n             到达时间：11:40。（仅供参考！）\\r\\n套餐D：自行前往舟山；\\r\\n行程自由安排。14：00后可自行前往酒店办理住宿。]]>\",\"tripStay\":\"<![CDATA[其他]]>\"},{\"tripDay\":2,\"tripTitle\":\"<![CDATA[舟山---全国各地]]>\",\"tripInfo\":\"<![CDATA[行程自由安排。\\r\\n12：00需前往酒店前台办理离店。\\r\\n套餐A：自行返回；\\r\\n套餐B：上车地点：朱家尖蜈蚣峙码头停车（前一天下车地点）；\\r\\n             发车时间：13：30。（准时发车，过时不候，谢谢您的配合！）；\\r\\n             下车地点：17：30（到达时间仅供参考）抵达杭州解百新世纪商厦（距离西湖风景区800米左右）\\r\\n套餐C：上车地点：朱家尖蜈蚣峙码头停车（前一天下车地点）；\\r\\n             发车时间：13：30。（18：40左右抵达上海人民广场）。\\r\\n             下车地点：18：40（到达时间仅供参考）抵达上海南站/上海人民广场。\\r\\n套餐D：自行返回；\\r\\n【回程大巴车牌号码，当天上午11点前未收更换的车牌号码，还是原来的车牌号码。】\\r\\n温馨提示：因天气、堵车等情况，大巴有可能会延误，如果赶飞机或者火车等请慎拍！否则一旦误机、误点等情况，我社概不负责！]]>\",\"breakfast\":\"<![CDATA[早餐]]>\"}],\"packageInclude\":{\"hotelTypes\":[{\"hotelID\":\"\"}],\"ticketTypes\":[{\"ticketID\":\"\",\"ticketTips\":{\"entertLimit\":{\"limitFlag\":\"\"}},\"attractions\":[{\"prodViewID\":\"\"}]}]},\"additionals\":[{}],\"linkeds\":[{}]},\"bookLimit\":{\"emergency\":\"false\",\"booker\":{\"name\":\"true\",\"mobile\":\"true\",\"email\":\"true\"},\"traveller\":{\"name\":\"TRAV_NUM_ALL\",\"enName\":\"TRAV_NUM_NO\",\"mobile\":\"TRAV_NUM_ALL\",\"email\":\"TRAV_NUM_ALL\",\"personType\":\"TRAV_NUM_ONE\",\"credentials\":\"TRAV_NUM_ALL\",\"credentialsType\":\"ID_CARD\",\"gender\":\"\",\"birthday\":\"\"}}}]}";
        /*Pattern p = Pattern.compile(".*<!\\[CDATA\\[(.*)\\]\\]>.*");
        Matcher m = p.matcher(s);

        if(m.matches()){
            System.out.println("m.group() = " + m.group(1));
        }*/
        String s1 = s.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]>", "");
        System.out.println("JsonUtils.toJSON(null) = " + JsonUtils.toJSON(null));
        /*JSONObject jsonObject = JsonUtils.toJSON(s1);
        jsonObject.remove("state");
        jsonObject.remove("totalCount");
        jsonObject.put("totalCount","totalCount");
        String s2 = jsonObject.toJSONString();
        System.out.println("s = " + s2);
        String s3 = JsonUtils.toString(new Object());
        JSONObject jsonObject1 = JsonUtils.toJSON(s3);
        jsonObject1.put("list", jsonObject);
        System.out.println("jsonObject1.toJSONString() = " + jsonObject1.toJSONString());*/

        /*Map<String, Object> result = new HashMap<String, Object>();
        result.put("Request", "getData");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "2018-01-01");
        map.put("name", "jack");
        result.put("Data", map);
        String xmlByMap = createXmlByMap(result);
        System.out.println("xmlByMap = " + formatXML(xmlByMap));
        Map<String, Object> mapByXml = createMapByXml(xmlByMap);
        System.out.println("mapByXml = " + mapByXml);*/
      /*  System.out.println(createXmlByMap(result));
        System.out.println(createXmlByMap("Parent", result));
        System.out.println(createXmlByMap("Parent", result, true));
        System.out.println(createMapByXml(createXmlByMap(result)));*/
      /*String data = "{\"order\":{\"partnerOrderNo\":\"10000\",\"orderId\":\"20014144\"}}";
        JSONObject jsonObject = JsonUtils.toJSON(data);
        String order = jsonObject.getString("order");
        JSONObject orderJson = JsonUtils.toJSON(order);
        String orderSn = orderJson.getString("partnerOrderNo");
        String  vorderSn = "5585888";
        if(StringUtils.isEmpty(vorderSn)) vorderSn = "6666666";
        if(StringUtils.isEmpty(vorderSn)) System.out.println("vorderSn = " + vorderSn);
        orderJson.put("partnerOrderNo",vorderSn);
        JSONObject json = JsonUtils.toJSON(new Object());

        json.put("order",orderJson);
        System.out.println("json = " + JsonUtils.toString(json));*/
        Goods goods = new Goods();
        goods.setId(1000L);
        goods.setNodeName("sss");
        JSONObject jsonObject = JsonUtils.toJSON(new Object());
        jsonObject.put("ddd", goods);
        System.out.println("jsonObject = " + JsonUtils.toString(jsonObject));

        String json = "{\n" +
                "    \"state\": {\n" +
                "      \"code\": \"1000\",\n" +
                "      \"message\": \"成功\",\n" +
                "      \"solution\": \"接口访问成功\"\n" +
                "    },\n" +
                "    \"order\": {\n" +
                "      \"orderId\": \"10000\",\n" +
                "      \"status\": \"NORMAL\",\n" +
                "      \"paymentStatus\": \"UNPAY\",\n" +
                "      \"orderMoney\": \"6000\",\n" +
                "      \"money\": \"1024\"\n" +
                "    }\n" +
                "  }\n";
        JSONObject json1 = JsonUtils.toJSON(json);
        json1.remove("state");
        String order = json1.getString("order");
        System.out.println("json1.toJSONString() = " + order);
        String product = "{\n" +
                "        \"packageId\":1006731,\n" +
                "        \"packageName\":\"自由行测试供应商打包+A行程\",\n" +
                "        \"price\":[\n" +
                "            {\n" +
                "                \"date\":\"2019-03-07\",\n" +
                "                \"marketPrice\":120,\n" +
                "                \"adultPrice\":100,\n" +
                "                \"childPrice\":\"100\",\n" +
                "                \"roomPrice\":\"100\",\n" +
                "                \"stock\":-1,\n" +
                "                \"aheadBookTime\":{\n" +
                "                    \"aheadDay\":0,\n" +
                "                    \"aheadTime\":\"00:00\"\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"date\":\"2019-03-08\",\n" +
                "                \"marketPrice\":120,\n" +
                "                \"adultPrice\":100,\n" +
                "                \"childPrice\":\"100\",\n" +
                "                \"roomPrice\":\"100\",\n" +
                "                \"stock\":-1,\n" +
                "                \"aheadBookTime\":{\n" +
                "                    \"aheadDay\":0,\n" +
                "                    \"aheadTime\":\"00:00\"\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"date\":\"2019-03-05\",\n" +
                "                \"marketPrice\":120,\n" +
                "                \"adultPrice\":100,\n" +
                "                \"childPrice\":\"100\",\n" +
                "                \"roomPrice\":\"100\",\n" +
                "                \"stock\":-1,\n" +
                "                \"aheadBookTime\":{\n" +
                "                    \"aheadDay\":0,\n" +
                "                    \"aheadTime\":\"00:00\"\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"date\":\"2019-03-06\",\n" +
                "                \"marketPrice\":120,\n" +
                "                \"adultPrice\":100,\n" +
                "                \"childPrice\":\"\",\n" +
                "                \"roomPrice\":\"100\",\n" +
                "                \"stock\":-1,\n" +
                "                \"aheadBookTime\":{\n" +
                "                    \"aheadDay\":0,\n" +
                "                    \"aheadTime\":\"00:00\"\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"date\":\"2019-03-09\",\n" +
                "                \"marketPrice\":120,\n" +
                "                \"adultPrice\":100,\n" +
                "                \"childPrice\":\"100\",\n" +
                "                \"roomPrice\":\"100\",\n" +
                "                \"stock\":-1,\n" +
                "                \"aheadBookTime\":{\n" +
                "                    \"aheadDay\":0,\n" +
                "                    \"aheadTime\":\"00:00\"\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"date\":\"2019-03-10\",\n" +
                "                \"marketPrice\":120,\n" +
                "                \"adultPrice\":100,\n" +
                "                \"childPrice\":\"100\",\n" +
                "                \"roomPrice\":\"100\",\n" +
                "                \"stock\":-1,\n" +
                "                \"aheadBookTime\":{\n" +
                "                    \"aheadDay\":0,\n" +
                "                    \"aheadTime\":\"00:00\"\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    }";
        String s2 = product.replaceAll("\\<!\\[CDATA\\[", "").replaceAll("\\]\\]\\>", "");
        JSONObject json2 = JsonUtils.toJSON(s2);
        String priceList = json2.getString("priceList");

    }
}