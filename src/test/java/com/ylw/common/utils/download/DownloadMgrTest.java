package com.ylw.common.utils.download;

import static org.junit.Assert.*;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ylw.common.utils.FileUtil;
import com.ylw.common.utils.ZipUtil;

public class DownloadMgrTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// https://pan.baidu.com/s/1boYKfld?errno=0&errmsg=Auth%20Login%20Sucess&&bduss=&ssnerror=0

	String json = "[{\"id\":\"48846026478682112\",\"url\":\"http://fs.up366.cn/download/48846026478682112?source=false\"},{\"id\":\"48846982407159808\",\"url\":\"http://fs.up366.cn/download/48846982407159808?source=false\"},{\"id\":\"48846031852994560\",\"url\":\"http://fs.up366.cn/download/48846031852994560?source=false\"},{\"id\":\"48846033084350464\",\"url\":\"http://fs.up366.cn/download/48846033084350464?source=false\"},{\"id\":\"48846043505819648\",\"url\":\"http://fs.up366.cn/download/48846043505819648?source=false\"},{\"id\":\"48846050120237056\",\"url\":\"http://fs.up366.cn/download/48846050120237056?source=false\"},{\"id\":\"48846052193337344\",\"url\":\"http://fs.up366.cn/download/48846052193337344?source=false\"},{\"id\":\"48846053736972288\",\"url\":\"http://fs.up366.cn/download/48846053736972288?source=false\"},{\"id\":\"48846056713093120\",\"url\":\"http://fs.up366.cn/download/48846056713093120?source=false\"},{\"id\":\"48846065773019136\",\"url\":\"http://fs.up366.cn/download/48846065773019136?source=false\"},{\"id\":\"48846067175751680\",\"url\":\"http://fs.up366.cn/download/48846067175751680?source=false\"},{\"id\":\"48846075104755712\",\"url\":\"http://fs.up366.cn/download/48846075104755712?source=false\"},{\"id\":\"48846077649682432\",\"url\":\"http://fs.up366.cn/download/48846077649682432?source=false\"},{\"id\":\"48846145606975488\",\"url\":\"http://fs.up366.cn/download/48846145606975488?source=false\"},{\"id\":\"48846080461111296\",\"url\":\"http://fs.up366.cn/download/48846080461111296?source=false\"},{\"id\":\"48846081391525888\",\"url\":\"http://fs.up366.cn/download/48846081391525888?source=false\"},{\"id\":\"48846131079839744\",\"url\":\"http://fs.up366.cn/download/48846131079839744?source=false\"},{\"id\":\"48846139699757056\",\"url\":\"http://fs.up366.cn/download/48846139699757056?source=false\"},{\"id\":\"48846040834703360\",\"url\":\"http://fs.up366.cn/download/48846040834703360?source=false\"},{\"id\":\"48846143221694464\",\"url\":\"http://fs.up366.cn/download/48846143221694464?source=false\"},{\"id\":\"48846110614716416\",\"url\":\"http://fs.up366.cn/download/48846110614716416?source=false\"},{\"id\":\"48846022758793216\",\"url\":\"http://fs.up366.cn/download/48846022758793216?source=false\"},{\"id\":\"48846066782044160\",\"url\":\"http://fs.up366.cn/download/48846066782044160?source=false\"},{\"id\":\"48846286136868864\",\"url\":\"http://fs.up366.cn/download/48846286136868864?source=false\"},{\"id\":\"48846294894706688\",\"url\":\"http://fs.up366.cn/download/48846294894706688?source=false\"},{\"id\":\"48846302317182976\",\"url\":\"http://fs.up366.cn/download/48846302317182976?source=false\"},{\"id\":\"48846307763257344\",\"url\":\"http://fs.up366.cn/download/48846307763257344?source=false\"},{\"id\":\"48844194827304960\",\"url\":\"http://fs.up366.cn/download/48844194827304960?source=false\"},{\"id\":\"48844193397309440\",\"url\":\"http://fs.up366.cn/download/48844193397309440?source=false\"},{\"id\":\"48846310749863936\",\"url\":\"http://fs.up366.cn/download/48846310749863936?source=false\"},{\"id\":\"48844121694044160\",\"url\":\"http://fs.up366.cn/download/48844121694044160?source=false\"},{\"id\":\"48844119418765312\",\"url\":\"http://fs.up366.cn/download/48844119418765312?source=false\"},{\"id\":\"48846069121351680\",\"url\":\"http://fs.up366.cn/download/48846069121351680?source=false\"},{\"id\":\"48846261370191872\",\"url\":\"http://fs.up366.cn/download/48846261370191872?source=false\"},{\"id\":\"48844037393350656\",\"url\":\"http://fs.up366.cn/download/48844037393350656?source=false\"},{\"id\":\"48846110920310784\",\"url\":\"http://fs.up366.cn/download/48846110920310784?source=false\"},{\"id\":\"48846119488618496\",\"url\":\"http://fs.up366.cn/download/48846119488618496?source=false\"},{\"id\":\"48844011682725888\",\"url\":\"http://fs.up366.cn/download/48844011682725888?source=false\"},{\"id\":\"48844245340356608\",\"url\":\"http://fs.up366.cn/download/48844245340356608?source=false\"},{\"id\":\"48844183600922624\",\"url\":\"http://fs.up366.cn/download/48844183600922624?source=false\"},{\"id\":\"48844187330412544\",\"url\":\"http://fs.up366.cn/download/48844187330412544?source=false\"},{\"id\":\"48844056629051392\",\"url\":\"http://fs.up366.cn/download/48844056629051392?source=false\"},{\"id\":\"48844063713525760\",\"url\":\"http://fs.up366.cn/download/48844063713525760?source=false\"},{\"id\":\"48844760249106432\",\"url\":\"http://fs.up366.cn/download/48844760249106432?source=false\"},{\"id\":\"48844196668243968\",\"url\":\"http://fs.up366.cn/download/48844196668243968?source=false\"},{\"id\":\"48844198283214848\",\"url\":\"http://fs.up366.cn/download/48844198283214848?source=false\"},{\"id\":\"48844202478043136\",\"url\":\"http://fs.up366.cn/download/48844202478043136?source=false\"},{\"id\":\"48846604274368512\",\"url\":\"http://fs.up366.cn/download/48846604274368512?source=false\"},{\"id\":\"48844205310377984\",\"url\":\"http://fs.up366.cn/download/48844205310377984?source=false\"},{\"id\":\"48846094331936768\",\"url\":\"http://fs.up366.cn/download/48846094331936768?source=false\"},{\"id\":\"48846100354433024\",\"url\":\"http://fs.up366.cn/download/48846100354433024?source=false\"},{\"id\":\"48846101826961408\",\"url\":\"http://fs.up366.cn/download/48846101826961408?source=false\"},{\"id\":\"48846105804308480\",\"url\":\"http://fs.up366.cn/download/48846105804308480?source=false\"},{\"id\":\"48846108224192512\",\"url\":\"http://fs.up366.cn/download/48846108224192512?source=false\"},{\"id\":\"48846110686838784\",\"url\":\"http://fs.up366.cn/download/48846110686838784?source=false\"},{\"id\":\"48846052925210624\",\"url\":\"http://fs.up366.cn/download/48846052925210624?source=false\"},{\"id\":\"48846155514839040\",\"url\":\"http://fs.up366.cn/download/48846155514839040?source=false\"},{\"id\":\"48838699311136768\",\"url\":\"http://fs.up366.cn/download/48838699311136768?source=false\"},{\"id\":\"48844508040167424\",\"url\":\"http://fs.up366.cn/download/48844508040167424?source=false\"},{\"id\":\"48838701479723008\",\"url\":\"http://fs.up366.cn/download/48838701479723008?source=false\"},{\"id\":\"48834886274646016\",\"url\":\"http://fs.up366.cn/download/48834886274646016?source=false\"},{\"id\":\"48847522776285184\",\"url\":\"http://fs.up366.cn/download/48847522776285184?source=false\"},{\"id\":\"48838705702961152\",\"url\":\"http://fs.up366.cn/download/48838705702961152?source=false\"},{\"id\":\"48838706289377280\",\"url\":\"http://fs.up366.cn/download/48838706289377280?source=false\"},{\"id\":\"48838707405914112\",\"url\":\"http://fs.up366.cn/download/48838707405914112?source=false\"},{\"id\":\"48838708233207808\",\"url\":\"http://fs.up366.cn/download/48838708233207808?source=false\"},{\"id\":\"48838712590303232\",\"url\":\"http://fs.up366.cn/download/48838712590303232?source=false\"},{\"id\":\"48838714314522624\",\"url\":\"http://fs.up366.cn/download/48838714314522624?source=false\"},{\"id\":\"48838714927251456\",\"url\":\"http://fs.up366.cn/download/48838714927251456?source=false\"},{\"id\":\"48838715585036288\",\"url\":\"http://fs.up366.cn/download/48838715585036288?source=false\"},{\"id\":\"48838716110897152\",\"url\":\"http://fs.up366.cn/download/48838716110897152?source=false\"},{\"id\":\"48838716810395648\",\"url\":\"http://fs.up366.cn/download/48838716810395648?source=false\"},{\"id\":\"48838717472309248\",\"url\":\"http://fs.up366.cn/download/48838717472309248?source=false\"},{\"id\":\"48838718198775808\",\"url\":\"http://fs.up366.cn/download/48838718198775808?source=false\"},{\"id\":\"48838719026429952\",\"url\":\"http://fs.up366.cn/download/48838719026429952?source=false\"},{\"id\":\"48838719610290176\",\"url\":\"http://fs.up366.cn/download/48838719610290176?source=false\"},{\"id\":\"48838720214794240\",\"url\":\"http://fs.up366.cn/download/48838720214794240?source=false\"},{\"id\":\"48838720895844352\",\"url\":\"http://fs.up366.cn/download/48838720895844352?source=false\"},{\"id\":\"48838721553825792\",\"url\":\"http://fs.up366.cn/download/48838721553825792?source=false\"},{\"id\":\"48838722092630016\",\"url\":\"http://fs.up366.cn/download/48838722092630016?source=false\"},{\"id\":\"48838722652274688\",\"url\":\"http://fs.up366.cn/download/48838722652274688?source=false\"},{\"id\":\"48838723514433536\",\"url\":\"http://fs.up366.cn/download/48838723514433536?source=false\"},{\"id\":\"48838724240343040\",\"url\":\"http://fs.up366.cn/download/48838724240343040?source=false\"},{\"id\":\"48838724822958080\",\"url\":\"http://fs.up366.cn/download/48838724822958080?source=false\"},{\"id\":\"48838725467832320\",\"url\":\"http://fs.up366.cn/download/48838725467832320?source=false\"},{\"id\":\"48838726091505664\",\"url\":\"http://fs.up366.cn/download/48838726091505664?source=false\"},{\"id\":\"48838726794444800\",\"url\":\"http://fs.up366.cn/download/48838726794444800?source=false\"},{\"id\":\"48838728891039744\",\"url\":\"http://fs.up366.cn/download/48838728891039744?source=false\"},{\"id\":\"48838729404416000\",\"url\":\"http://fs.up366.cn/download/48838729404416000?source=false\"},{\"id\":\"48838729934405632\",\"url\":\"http://fs.up366.cn/download/48838729934405632?source=false\"},{\"id\":\"48838730551328768\",\"url\":\"http://fs.up366.cn/download/48838730551328768?source=false\"},{\"id\":\"48838731147771904\",\"url\":\"http://fs.up366.cn/download/48838731147771904?source=false\"},{\"id\":\"48838731964252160\",\"url\":\"http://fs.up366.cn/download/48838731964252160?source=false\"},{\"id\":\"48838732685967360\",\"url\":\"http://fs.up366.cn/download/48838732685967360?source=false\"},{\"id\":\"48838733197246464\",\"url\":\"http://fs.up366.cn/download/48838733197246464?source=false\"},{\"id\":\"48838733806370816\",\"url\":\"http://fs.up366.cn/download/48838733806370816?source=false\"},{\"id\":\"48838734672232448\",\"url\":\"http://fs.up366.cn/download/48838734672232448?source=false\"},{\"id\":\"48847043370713088\",\"url\":\"http://fs.up366.cn/download/48847043370713088?source=false\"},{\"id\":\"48838736764207104\",\"url\":\"http://fs.up366.cn/download/48838736764207104?source=false\"},{\"id\":\"48838737730306048\",\"url\":\"http://fs.up366.cn/download/48838737730306048?source=false\"},{\"id\":\"48838738350899200\",\"url\":\"http://fs.up366.cn/download/48838738350899200?source=false\"},{\"id\":\"48838738946162688\",\"url\":\"http://fs.up366.cn/download/48838738946162688?source=false\"},{\"id\":\"48838739831521280\",\"url\":\"http://fs.up366.cn/download/48838739831521280?source=false\"},{\"id\":\"48838740750204928\",\"url\":\"http://fs.up366.cn/download/48838740750204928?source=false\"},{\"id\":\"48838741965504512\",\"url\":\"http://fs.up366.cn/download/48838741965504512?source=false\"},{\"id\":\"48838742735814656\",\"url\":\"http://fs.up366.cn/download/48838742735814656?source=false\"},{\"id\":\"48838743386095616\",\"url\":\"http://fs.up366.cn/download/48838743386095616?source=false\"},{\"id\":\"48838743986470912\",\"url\":\"http://fs.up366.cn/download/48838743986470912?source=false\"},{\"id\":\"48838744552308736\",\"url\":\"http://fs.up366.cn/download/48838744552308736?source=false\"},{\"id\":\"48838745135775744\",\"url\":\"http://fs.up366.cn/download/48838745135775744?source=false\"},{\"id\":\"48838746729119744\",\"url\":\"http://fs.up366.cn/download/48838746729119744?source=false\"},{\"id\":\"48838747575517184\",\"url\":\"http://fs.up366.cn/download/48838747575517184?source=false\"},{\"id\":\"48838748415262720\",\"url\":\"http://fs.up366.cn/download/48838748415262720?source=false\"},{\"id\":\"48838749917544448\",\"url\":\"http://fs.up366.cn/download/48838749917544448?source=false\"},{\"id\":\"48838750533091328\",\"url\":\"http://fs.up366.cn/download/48838750533091328?source=false\"},{\"id\":\"48838752035078144\",\"url\":\"http://fs.up366.cn/download/48838752035078144?source=false\"},{\"id\":\"48838752651345920\",\"url\":\"http://fs.up366.cn/download/48838752651345920?source=false\"},{\"id\":\"48838753502232576\",\"url\":\"http://fs.up366.cn/download/48838753502232576?source=false\"},{\"id\":\"48838754369273856\",\"url\":\"http://fs.up366.cn/download/48838754369273856?source=false\"},{\"id\":\"48838755436199936\",\"url\":\"http://fs.up366.cn/download/48838755436199936?source=false\"},{\"id\":\"48838756560797696\",\"url\":\"http://fs.up366.cn/download/48838756560797696?source=false\"},{\"id\":\"48838709333131264\",\"url\":\"http://fs.up366.cn/download/48838709333131264?source=false\"},{\"id\":\"48838710136864768\",\"url\":\"http://fs.up366.cn/download/48838710136864768?source=false\"},{\"id\":\"48838710943219712\",\"url\":\"http://fs.up366.cn/download/48838710943219712?source=false\"},{\"id\":\"48838711566794752\",\"url\":\"http://fs.up366.cn/download/48838711566794752?source=false\"},{\"id\":\"48838727375912960\",\"url\":\"http://fs.up366.cn/download/48838727375912960?source=false\"},{\"id\":\"48838728110505984\",\"url\":\"http://fs.up366.cn/download/48838728110505984?source=false\"}]";

	@Test
	public void testAddDownloadInfo() {
		// fail("Not yet implemented");
		String path = "C:\\Users\\y\\Desktop\\git merge test\\";
		 DownloadInfo info = new DownloadInfo();
		 info.setName("48846982407159808");
		 info.setUrl("http://fs.up366.cn/download/48846982407159808?source=false");
		 info.setSavePath(path + "xx.zip");
		 new DownloadMgr().download(info);
		 ZipUtil.unzip(info.getSavePath(), path + info.getName()+"//");

		JSONArray jsonArray = JSON.parseArray(json);
		jsonArray.forEach(item -> {
			String id = (String) ((JSONObject) item).get("id");
			String paperPath = path + id + "//paper.xml";
			String paperXML = FileUtil.getString(paperPath);
			handleXML(paperXML);
		});
		
		FileUtil.saveFullPathFile(path+"wordIds.txt", sb.toString());
		System.out.println("over .............");

	}

	StringBuilder sb=new StringBuilder();
	private void handleXML(String paperXML) {
		// TODO Auto-generated method stub
		System.out.println(paperXML);
		Parser parse = Parser.createParser(paperXML, "UTF-8");
		NodeFilter filter = new NodeFilter() {
			
			private boolean inKnoledge;

			@Override
			public boolean accept(Node node) {
				// TODO Auto-generated method stub
//				System.out.println(node.getText());
				if (inKnoledge) {
					sb.append(node.toHtml());
					inKnoledge = false;
				}
				if ("<knowledge>".equals(node.toHtml())) {
					inKnoledge=true;
				}
				return false;
			}
		};
		try {
			NodeList nodes = parse.extractAllNodesThatMatch(filter);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
