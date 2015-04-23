import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.json.simple.JSONObject;

public class BaltimoreMuseumParser 
{
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException 
	{
			 
			Document docMainPg;
			int counter = 0;
			int urlcnt = 0;
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File ("output.json").getAbsolutePath()));
			//bw.write("{");
			
			bw.write("[");
			try 
			{
				// need http protocol
				//doc = Jsoup.connect("http://art.thewalters.org/browse/medium/painting--drawing/").get();
				//doc = Jsoup.connect("http://art.thewalters.org/detail/9087/big-bowl-a-crow-chief/").get();
				while( counter <= 350)
				{
				urlcnt++;
				String mainurl = "http://art.thewalters.org/browse/medium/painting--drawing/?page=" + String.valueOf(urlcnt);
				docMainPg = Jsoup.connect(mainurl).get();
				
				// get page title
				String title = docMainPg.title();
				System.out.println("title : " + title);
				
				//get all href's of all the paintings
				ArrayList<String> allPaintingsHref = new ArrayList<String>();
				Elements allHrefs = docMainPg.select("a[class=object]");
				for(Element e: allHrefs)
					allPaintingsHref.add(e.attr("href"));
				
				System.out.println("allPaintingsHref.size()= " +allPaintingsHref.size());
				
				
		/*		//Get href's of class="object"
				Elements links = doc.select("a[href]");
				System.out.println("No of links =" +links.size());
					for (Element link : links) 
					{
						System.out.println("\nlink : " + link.attr("href"));
						System.out.println("text : " + link.text());
					}	*/
				
			for(int k=0; k < allPaintingsHref.size();k++ )
			{
				Document doc =Jsoup.connect(allPaintingsHref.get(k)).get();
				
				String url=null;	
				Elements eUrl = doc.select("img[class=preload]");
				for(Element e: eUrl)
				{
					url = e.attr("src");
				}
				String creator=null;
				Elements eCreator = doc.select("div.column:nth-child(1) > ul:nth-child(2) > li:nth-child(1) > a:nth-child(1)");
				for(Element e: eCreator)
				{
					creator = e.text();
				}
			
				String paintingurl=null,paintingtitle=null;
				Elements epaintingurl = doc.select(".information > h1:nth-child(1) > a:nth-child(1)");
				for(Element e: epaintingurl)
				{
					paintingurl = e.attr("href");
					paintingtitle=e.text();
				}
			
				ArrayList<String> infoList = new ArrayList<String>();
				Elements info = doc.select("div[class=column]");
				//	Elements info = doc.select("h6[class=no_select]");
				for(Element i: info)
				{
					infoList.add(i.text());
				}
				
				
				if(creator.contains("American") || creator.contains("Italian") || creator.contains("French") ||
						creator.contains("Netherlandish") || creator.contains("Ethiopian") || creator.contains("Belgian") ||
						creator.contains("Dutch") || creator.contains("Flemish") || creator.contains("English") ||
						creator.contains("Austrian") || creator.contains("Egyptian") || creator.contains("Venetian") ||
						creator.contains("Spanish") || creator.contains("German,") || creator.contains("Flemish"))
					counter++;
				else
					break;
				System.out.println("Record: " +counter);
				System.out.println("Image URL = " +url);
				System.out.println("Creator = " +creator);
				System.out.println("Painting URL = " +paintingurl);
				System.out.println("Painting Title: " +paintingtitle);
				String period=null,medium=null,accessionNo=null,measurements=null;
				for(int i=0;i<infoList.size();i++)
				{
					System.out.println("Info[" +i+ "]= " +infoList.get(i));
					switch(i)
					{
						case 0: period=infoList.get(i).substring(7);
								break;
						case 1: medium=infoList.get(i).substring(7);
								break;
						case 2: accessionNo=infoList.get(i).substring(17);
								break;
						case 3: measurements=infoList.get(i).substring(11);
								break;
						
					}
				}
				
				JSONObject jObj = new JSONObject();
				
				//jObj.put(" Record - " +counter +",");
				//bw.newLine();
				//bw.write("Information:{");
				jObj.put("Painting Title", paintingtitle);
				//bw.newLine();
				jObj.put("Painting URL", paintingurl);
				//bw.newLine();
				jObj.put("Image URL ", url);
				//bw.newLine();
				jObj.put("Period" ,period);
				//bw.newLine();
				jObj.put("Medium" ,medium);
				//bw.newLine();
				jObj.put("Accession Number", accessionNo);
				//bw.newLine();
				jObj.put("Measurements ", measurements);
				jObj.put("Creator ", creator);
				//bw.write("  },");
				//bw.newLine();
				
				
				bw.write(jObj.toJSONString());
				bw.write(",");
				
				
				//bw.write(" Record: " +counter +",");
				//bw.newLine();
				//bw.write("Information:{");
				//bw.write("Painting Title: " +paintingtitle +",");
				//bw.newLine();
				//bw.write("Painting URL: " +paintingurl +",");
				//bw.newLine();
				//bw.write("Image URL: " +url +",");
				//bw.newLine();
				//bw.write("Period: " +period +",");
				//bw.newLine();
				//bw.write("Medium: " +medium +",");
				//bw.newLine();
				//bw.write("Accession Number: " +accessionNo +",");
				//bw.newLine();
				//bw.write("Measurements: " +measurements);				
				//bw.write("  },");
				//bw.newLine();
			}
			}
				bw.write("]");	
				bw.close();
				System.out.println("urlcnt = " +urlcnt+ "counter = " +counter);
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
	}

}
