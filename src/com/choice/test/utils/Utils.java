package com.choice.test.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.choice.test.domain.Arear;
import com.choice.test.domain.Card;
import com.choice.test.domain.CardRules;
import com.choice.test.domain.CardTyp;
import com.choice.test.domain.ChargeRecord;
import com.choice.test.domain.City;
import com.choice.test.domain.ConsumeRecord;
import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.FavorArea;
import com.choice.test.domain.Firm;
import com.choice.test.domain.ItemPrgPackage;
import com.choice.test.domain.ItemPrgpackAgedtl;
import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_Orders;
import com.choice.test.domain.Project;
import com.choice.test.domain.ProjectType;
import com.choice.test.domain.Sft;
import com.choice.test.domain.StoreTable;
import com.choice.test.domain.Voucher;
import com.choice.test.domain.WebMsg;

public class Utils {
	 //获取会员信息
	 public static List<Card> findCard(String data){
		 	 List<Card> listCard=new ArrayList<Card>();
	 		 //创建一个新的字符串
	         StringReader read = new StringReader(data);
	         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
	         InputSource source = new InputSource(read);
	         //创建一个新的SAXBuilder
	         SAXBuilder sbil = new SAXBuilder();
	         try {
	             //通过输入源构造一个Document
	             Document doc = sbil.build(source);
	             //取的根元素
	             Element root = doc.getRootElement();
//	             System.out.println(root.getName());//输出根元素的名称（测试）
	             //得到根元素所有子元素的集合
	             List jiedian = root.getChildren();
	             //获得XML中的命名空间（XML中未定义可不写）
	             Namespace ns = root.getNamespace();
	             Element et = null;
	             for(int i=0;i<jiedian.size();i++){
	                 et = (Element) jiedian.get(i);//循环依次得到子元素
	                 List sun = et.getChildren();
	                 for(int j=0;j<sun.size();j++){
	                 	et = (Element) sun.get(j);//循环依次得到孙元素
	                 	Card card=new Card();
	                 	card.setCardNo(et.getAttributeValue("cardNo"));
	                 	card.setName(et.getAttributeValue("name"));
	                 	card.setzAmt(et.getAttributeValue("zAmt"));
	                 	card.setTtlFen(et.getAttributeValue("ttlFen"));
	                 	card.setCredit(et.getAttributeValue("credit"));
	                 	card.setTele(et.getAttributeValue("tele"));
	                 	card.setTypDes(et.getAttributeValue("typDes"));
	                 	card.setRunNum(et.getAttributeValue("rannum"));
	                 	card.setQrordr(et.getAttributeValue("cardNo")+et.getAttributeValue("rannum"));
	                 	listCard.add(card);
	                 }
	             }
	             
	         } catch (JDOMException e) {
	             e.printStackTrace();
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	         return listCard;
	 }
	//获取会员卡类型
	 public static List<CardTyp> getCardTyp(String data){
		 	 List<CardTyp> listCardTyp=new ArrayList<CardTyp>();
	 		 //创建一个新的字符串
	         StringReader read = new StringReader(data);
	         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
	         InputSource source = new InputSource(read);
	         //创建一个新的SAXBuilder
	         SAXBuilder sbil = new SAXBuilder();
	         try {
	             //通过输入源构造一个Document
	             Document doc = sbil.build(source);
	             //取的根元素
	             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
	             //得到根元素所有子元素的集合
	             List jiedian = root.getChildren();
	             //获得XML中的命名空间（XML中未定义可不写）
	             Namespace ns = root.getNamespace();
	             Element et = null;
	             for(int i=0;i<jiedian.size();i++){
	                 et = (Element) jiedian.get(i);//循环依次得到子元素
	                 List sun = et.getChildren();
	                 for(int j=0;j<sun.size();j++){
	                 	et = (Element) sun.get(j);//循环依次得到孙元素
	                 	CardTyp cardTyp=new CardTyp();
	                 	cardTyp.setId(et.getAttributeValue("id"));
	                 	cardTyp.setNam(et.getAttributeValue("nam"));
	                 	listCardTyp.add(cardTyp);
	                 }
	             }
	             
	         } catch (JDOMException e) {
	             e.printStackTrace();
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	         return listCardTyp;
	 }
	 //获取电子卷信息
	 public static List<Voucher> findVoucher(String data){
	 	 List<Voucher> listVoucher=new ArrayList<Voucher>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	Voucher voucher=new Voucher();
                 	voucher.setId(et.getAttributeValue("id"));
                 	voucher.setTypdes(et.getAttributeValue("typdes"));
                 	voucher.setFirmname(et.getAttributeValue("firmname"));
                 	voucher.setBdate(et.getAttributeValue("bdate"));
                 	voucher.setEdate(et.getAttributeValue("edate"));
                 	listVoucher.add(voucher);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listVoucher;
	 }
	//获取会员卡规则
	 public static List<CardRules> findCardRules(String data){
	 	 List<CardRules> listCardRules=new ArrayList<CardRules>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//	             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	CardRules cardRules=new CardRules();
                 	cardRules.setID(et.getAttributeValue("ID"));
                 	cardRules.setChgrules(et.getAttributeValue("chgrules"));
                 	cardRules.setJifenrules(et.getAttributeValue("jifenrules"));
                 	cardRules.setExclusprivle(et.getAttributeValue("exclusprivle"));
                 	cardRules.setCardexplan(et.getAttributeValue("cardexplan"));
                 	cardRules.setCardtele(et.getAttributeValue("cardtele"));
                 	cardRules.setSTORE(et.getAttributeValue("STORE"));
                 	listCardRules.add(cardRules);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listCardRules;
	 }
	//获取充值记录
	 public static List<ChargeRecord> findChargeRecord(String data){
	 	 List<ChargeRecord> listChargeRecord=new ArrayList<ChargeRecord>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	ChargeRecord chargeRecord=new ChargeRecord();
                 	chargeRecord.setCardno(et.getAttributeValue("cardno"));
                 	chargeRecord.setTim(et.getAttributeValue("tim"));
                 	chargeRecord.setRmbamt(et.getAttributeValue("rmbamt"));
                 	chargeRecord.setGiftamt(et.getAttributeValue("giftamt"));
                 	chargeRecord.setOperater(et.getAttributeValue("operater"));
                 	chargeRecord.setPayment(et.getAttributeValue("payment"));
                 	listChargeRecord.add(chargeRecord);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listChargeRecord;
	 }
	//获取消费记录
	 public static List<ConsumeRecord> findConsumeRecord(String data){
	 	 List<ConsumeRecord> listConsumeRecord=new ArrayList<ConsumeRecord>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	ConsumeRecord consumeRecord=new ConsumeRecord();
                 	consumeRecord.setCardno(et.getAttributeValue("cardno"));
                 	consumeRecord.setTim(et.getAttributeValue("tim"));
                 	consumeRecord.setAmt(et.getAttributeValue("amt"));
                 	consumeRecord.setBalaamt(et.getAttributeValue("balaamt"));
                 	consumeRecord.setFirmdes(et.getAttributeValue("firmdes"));
                 	consumeRecord.setQczamt(et.getAttributeValue("qczamt"));
                 	consumeRecord.setQmzamt(et.getAttributeValue("qmzamt"));
                 	listConsumeRecord.add(consumeRecord);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listConsumeRecord;
	 }
	 //获取门店信息
	 public static List<Firm> findFirm(String data){
	 	 List<Firm> listFirm=new ArrayList<Firm>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//	             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	Firm firm=new Firm();
                 	firm.setFirmid(et.getAttributeValue("firmid"));
                 	firm.setFirmdes(et.getAttributeValue("firmdes"));
                 	firm.setInit(et.getAttributeValue("init"));
                 	firm.setArea(et.getAttributeValue("area"));
                 	firm.setAddr(et.getAttributeValue("addr"));
                 	firm.setTele(et.getAttributeValue("tele"));
                 	firm.setWbigpic(et.getAttributeValue("wbigpic"));
                 	listFirm.add(firm);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listFirm;
	 }

	 //菜品类别
	 public static List<ProjectType> findProjectTyp(String data){
		 List<ProjectType> listProjectType=new ArrayList<ProjectType>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	ProjectType projectType=new ProjectType();
                 	projectType.setId(et.getAttributeValue("id"));
                 	projectType.setDes(et.getAttributeValue("des"));
                 	projectType.setCode(et.getAttributeValue("code"));
                 	projectType.setFirmid(et.getAttributeValue("firmid"));
                 	projectType.setTypind(et.getAttributeValue("typind"));
                 	listProjectType.add(projectType);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listProjectType;
	 }

	//获取菜品
	 public static List<Project> findProject(String data){
		 List<Project> listProject=new ArrayList<Project>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	Project project=new Project();
                 	project.setPubitem(et.getAttributeValue("pubitem"));
                 	project.setPitcode(et.getAttributeValue("pitcode"));
                 	project.setPdes(et.getAttributeValue("pdes"));
                 	project.setPinit(et.getAttributeValue("pinit"));
                 	project.setPrice(et.getAttributeValue("price"));
                 	project.setUrl(et.getAttributeValue("url"));
                 	project.setSmallUrl(et.getAttributeValue("smallUrl"));
                 	listProject.add(project);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listProject;
	 }

	//获取区域
	 public static List<Arear> findArear(String data){
		 List<Arear> listArear=new ArrayList<Arear>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	Arear arear=new Arear();
                 	arear.setAreaid(et.getAttributeValue("areaid"));
                 	arear.setAreaname(et.getAttributeValue("areaname"));
                 	listArear.add(arear);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listArear;
	 }
	 
	//获取台位
	 public static List<StoreTable> findStoreTable(String data){
		 List<StoreTable> listStoreTable=new ArrayList<StoreTable>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	StoreTable storeTable=new StoreTable();
                 	storeTable.setTbl(et.getAttributeValue("tbl"));
                 	storeTable.setDes(et.getAttributeValue("des"));
                 	storeTable.setArea(et.getAttributeValue("area"));
                 	storeTable.setId(et.getAttributeValue("id"));
                 	storeTable.setFirmid(et.getAttributeValue("firmid"));
                 	storeTable.setInit(et.getAttributeValue("init"));
                 	storeTable.setPax(et.getAttributeValue("pax"));
                 	storeTable.setMincost(et.getAttributeValue("mincost"));
                 	storeTable.setRoomtyp(et.getAttributeValue("roomtyp"));
                 	listStoreTable.add(storeTable);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listStoreTable;
	 }
	//获取可预订台位
	 public static List<StoreTable> findTable(String data){
		 List<StoreTable> listStoreTable=new ArrayList<StoreTable>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//			             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	StoreTable storeTable=new StoreTable();
                 	storeTable.setPax(et.getAttributeValue("pax"));
                 	storeTable.setNum(et.getAttributeValue("num"));
                 	storeTable.setRoomtyp(et.getAttributeValue("roomtyp"));
                 	listStoreTable.add(storeTable);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listStoreTable;
	 }
	//获取可预订台位或包间
	 public static List<StoreTable> findResvTbl(String data){
		 List<StoreTable> listStoreTable=new ArrayList<StoreTable>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//				             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	StoreTable storeTable=new StoreTable();
                 	storeTable.setPax(et.getAttributeValue("pax"));
                 	storeTable.setId(et.getAttributeValue("ID"));
                 	storeTable.setRoomtyp(et.getAttributeValue("roomtyp"));
                 	storeTable.setDes(et.getAttributeValue("des"));
                 	storeTable.setTbl(et.getAttributeValue("tbl"));
                 	listStoreTable.add(storeTable);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listStoreTable;
	 }
	//获取我的订单隶属门店
	 public static List<Net_Orders> getOrderCity(String data){
		 List<Net_Orders> listNet_Orders=new ArrayList<Net_Orders>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//			             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	Net_Orders orders=new Net_Orders();
                 	orders.setFirmid(et.getAttributeValue("firmid"));
                 	orders.setFirmdes(et.getAttributeValue("firmdes"));
                 	orders.setDat(et.getAttributeValue("dat"));
                 	orders.setDatmins(et.getAttributeValue("datmins"));
                 	orders.setOpenid(et.getAttributeValue("openid"));
                 	listNet_Orders.add(orders);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listNet_Orders;
	 }
	 //获取我的订单
	 public static List<Net_Orders> getOrderMenus(String data){
		 List<Net_Orders> listNet_Orders=new ArrayList<Net_Orders>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	Net_Orders orders=new Net_Orders();
                 	orders.setId(et.getAttributeValue("id"));
                 	orders.setResv(et.getAttributeValue("resv"));
                 	orders.setOrderTimes(et.getAttributeValue("orderTimes"));
                 	orders.setFirmid(et.getAttributeValue("firmid"));
                 	orders.setFirmdes(et.getAttributeValue("firmdes"));
                 	orders.setDat(et.getAttributeValue("dat"));
                 	orders.setDatmins(et.getAttributeValue("datmins"));
                 	orders.setPax(et.getAttributeValue("pax"));
                 	orders.setSft(et.getAttributeValue("sft"));
                 	orders.setTables(et.getAttributeValue("tables"));
                 	orders.setContact(et.getAttributeValue("contact"));
                 	orders.setAddr(et.getAttributeValue("addr"));
                 	orders.setTele(et.getAttributeValue("tele"));
                 	orders.setOpenid(et.getAttributeValue("openid"));
                 	orders.setRannum(et.getAttributeValue("rannum"));
                 	orders.setRemark(et.getAttributeValue("remark"));
                 	listNet_Orders.add(orders);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listNet_Orders;
	 }
     //获取我的订单详情
	 public static List<Net_OrderDtl> getOrderDtlMenus(String data){
		 List<Net_OrderDtl> listNet_OrderDtl=new ArrayList<Net_OrderDtl>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	Net_OrderDtl orderDtl=new Net_OrderDtl();
                 	orderDtl.setId(et.getAttributeValue("id"));
                 	orderDtl.setOrdersid(et.getAttributeValue("ordersid"));
                 	orderDtl.setFoodsid(et.getAttributeValue("foodsid"));
                 	orderDtl.setFoodnum(et.getAttributeValue("foodnum"));
                 	orderDtl.setTotalprice(et.getAttributeValue("totalprice"));
                 	orderDtl.setFoodsname(et.getAttributeValue("foodsname"));
                 	orderDtl.setPrice(et.getAttributeValue("price"));
                 	orderDtl.setIspackage(et.getAttributeValue("ispackage"));
                 	listNet_OrderDtl.add(orderDtl);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listNet_OrderDtl;
	 }
	 
     //城市
	 public static List<City> getCity(String data){
		 List<City> listCity=new ArrayList<City>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	City city=new City();
                 	city.setSno(et.getAttributeValue("sno"));
                 	city.setDes(et.getAttributeValue("des"));
                 	listCity.add(city);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listCity;
	 }
     //餐次
	 public static List<Sft> getSft(String data){
		 List<Sft> listSft=new ArrayList<Sft>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	Sft sft=new Sft();
                 	sft.setId(et.getAttributeValue("id"));
                 	sft.setCode(et.getAttributeValue("code"));
                 	sft.setName(et.getAttributeValue("name"));
                 	listSft.add(sft);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listSft;
	 }
     //时间桌台，台位状态
	 public static List<DeskTimes> getDeskTimes(String data){
		 List<DeskTimes> listDeskTimes=new ArrayList<DeskTimes>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//		             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	DeskTimes deskTimes=new DeskTimes();
                 	deskTimes.setId(et.getAttributeValue("id"));
                 	deskTimes.setResvtblid(et.getAttributeValue("resvtblid"));
                 	deskTimes.setDat(et.getAttributeValue("dat"));
                 	deskTimes.setSft(et.getAttributeValue("sft"));
                 	deskTimes.setRemark(et.getAttributeValue("remark"));
                 	deskTimes.setState(et.getAttributeValue("state"));
                 	deskTimes.setOrdersid(et.getAttributeValue("ordersid"));
                 	listDeskTimes.add(deskTimes);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listDeskTimes;
	 }
	//查询优惠信息
	 public static List<WebMsg> findWebMsg(String data){
		 List<WebMsg> listWebMsg=new ArrayList<WebMsg>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//			             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	WebMsg webMsg=new WebMsg();
                 	webMsg.setId(et.getAttributeValue("id"));
                 	webMsg.setDat(et.getAttributeValue("dat"));
                 	webMsg.setTitle(et.getAttributeValue("title"));
                 	webMsg.setWurl(et.getAttributeValue("wurl"));
                 	webMsg.setWcontent(et.getAttributeValue("wcontent"));
                 	webMsg.setKeyword(et.getAttributeValue("keyword"));
                 	webMsg.setFirmid(et.getAttributeValue("firmid"));
                 	webMsg.setFirmnm(et.getAttributeValue("firmnm"));
                 	listWebMsg.add(webMsg);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listWebMsg;
	 }
	//查询优惠信息区域
	 public static List<FavorArea> findFavorArea(String data){
		 List<FavorArea> listFavorArea=new ArrayList<FavorArea>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//				             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	FavorArea favorArea=new FavorArea();
                 	favorArea.setId(et.getAttributeValue("id"));
                 	favorArea.setArea(et.getAttributeValue("area"));
                 	favorArea.setFirmid(et.getAttributeValue("firmid"));
                 	listFavorArea.add(favorArea);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listFavorArea;
	 }
	 /**************************************套餐*****************************************/
	//查询套餐
	 public static List<ItemPrgPackage> findItemPrgPackage(String data){
		 List<ItemPrgPackage> listItemPrgPackage=new ArrayList<ItemPrgPackage>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//				             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	ItemPrgPackage itemPrgPackage=new ItemPrgPackage();
                 	itemPrgPackage.setId(et.getAttributeValue("id"));
                 	itemPrgPackage.setPrgid(et.getAttributeValue("prgid"));
                 	itemPrgPackage.setPackages(et.getAttributeValue("packages"));
                 	itemPrgPackage.setDes(et.getAttributeValue("des"));
                 	itemPrgPackage.setPrice(et.getAttributeValue("price"));
                 	itemPrgPackage.setSno(et.getAttributeValue("sno"));
                 	itemPrgPackage.setSnodes(et.getAttributeValue("snodes"));
                 	itemPrgPackage.setFirmid(et.getAttributeValue("firmid"));
                 	itemPrgPackage.setPicsrc(et.getAttributeValue("picsrc"));
                 	listItemPrgPackage.add(itemPrgPackage);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listItemPrgPackage;
	 }
	//查询套餐明细
	 public static List<ItemPrgpackAgedtl> findItemPrgpackAgedtl(String data){
		 List<ItemPrgpackAgedtl> listItemPrgpackAgedtl=new ArrayList<ItemPrgpackAgedtl>();
 		 //创建一个新的字符串
         StringReader read = new StringReader(data);
         //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
         InputSource source = new InputSource(read);
         //创建一个新的SAXBuilder
         SAXBuilder sbil = new SAXBuilder();
         try {
             //通过输入源构造一个Document
             Document doc = sbil.build(source);
             //取的根元素
             Element root = doc.getRootElement();
//				             System.out.println(root.getName());//输出根元素的名称（测试）
             //得到根元素所有子元素的集合
             List jiedian = root.getChildren();
             //获得XML中的命名空间（XML中未定义可不写）
             Namespace ns = root.getNamespace();
             Element et = null;
             for(int i=0;i<jiedian.size();i++){
                 et = (Element) jiedian.get(i);//循环依次得到子元素
                 List sun = et.getChildren();
                 for(int j=0;j<sun.size();j++){
                 	et = (Element) sun.get(j);//循环依次得到孙元素
                 	ItemPrgpackAgedtl itemPrgpackAgedtl=new ItemPrgpackAgedtl();
                 	itemPrgpackAgedtl.setIdcode(et.getAttributeValue("idcode"));
                 	itemPrgpackAgedtl.setPrgid(et.getAttributeValue("prgid"));
                 	itemPrgpackAgedtl.setPubitem(et.getAttributeValue("pubitem"));
                 	itemPrgpackAgedtl.setPitcode(et.getAttributeValue("pitcode"));
                 	itemPrgpackAgedtl.setPackages(et.getAttributeValue("packages"));
                 	itemPrgpackAgedtl.setId(et.getAttributeValue("id"));
                 	itemPrgpackAgedtl.setItem(et.getAttributeValue("item"));
                 	itemPrgpackAgedtl.setDes(et.getAttributeValue("des"));
                 	itemPrgpackAgedtl.setCnt(et.getAttributeValue("cnt"));
                 	itemPrgpackAgedtl.setUnit(et.getAttributeValue("unit"));
                 	itemPrgpackAgedtl.setFirmid(et.getAttributeValue("firmid"));
                 	itemPrgpackAgedtl.setPackageid(et.getAttributeValue("packageid"));
                 	listItemPrgpackAgedtl.add(itemPrgpackAgedtl);
                 }
             }
             
         } catch (JDOMException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return listItemPrgpackAgedtl;
	 }

	 /**
	  * 会员卡id解密
	  * @param encrypt
	  * @return
	  */
	public static String decryptCardId(String encrypt) {
		StringBuilder sb = new StringBuilder("");
		byte[] bt = hexStringToBytes(encrypt);
		for(int i = 0; i < bt.length; i++) {
			char temp = (char) bt[i];
			temp = (char)(5 ^ temp);
			sb.append(temp);
		}
		
		return sb.toString();
	}
		
	/**  
	 * Convert hex string to byte[]  
	 * @param hexString the hex string  
	 * @return byte[]  
	 */  
	public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	}
	/**  
	 * Convert char to byte  
	 * @param c char  
	 * @return byte  
	 */  
	 private static byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}  
}
