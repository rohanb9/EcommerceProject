
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class SAXProductHandler extends DefaultHandler {

	// private List<Product> pData;

	boolean bproduct = false;
	boolean bname = false;
	boolean bprice = false;
	
	boolean bdiscount = false;

	boolean brdiscount = false;

	boolean brwarranty = false;
	boolean bcategory = false;
	
	boolean baccname = false;
	boolean baccprice = false;

	boolean baccdiscount = false;
	boolean bacc = false;

	boolean bretailer = false;
	boolean bretailerZip = false;
	boolean bretailerCity = false;
	boolean bretailerState = false;
	boolean bproductOnSale = false;
	boolean bmanufacturerName = false;
	
	Product product;
	Accessory acc;
	HashMap<String, Product> hpCatalog = new HashMap<String, Product>();
	HashMap<String, Accessory> accessoryMap = new HashMap<String, Accessory>();


	public HashMap<String, Product> readDataFromXML(String fileName)
			throws ParserConfigurationException, SAXException, IOException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		javax.xml.parsers.SAXParser parser = factory.newSAXParser();

		parser.parse(new File(fileName), this);

		return hpCatalog;
	}

	@Override
	public void startDocument() throws SAXException {
		System.out.println("Start document");
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println("End document");
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{
			
		if(qName.equalsIgnoreCase("product"))
		{
			bproduct = true;
			product = new Product();
		}
		
		if(bproduct)
		{
		
			if (qName.equalsIgnoreCase("name")) {
				bname = true;
			}

			else if (qName.equalsIgnoreCase("discount")) {
				bdiscount = true;
			}

			else if (qName.equalsIgnoreCase("price")) {
				bprice = true;
			}

			else if (qName.equalsIgnoreCase("rdiscount")) {
				brdiscount = true;
			}

			else if (qName.equalsIgnoreCase("rwarranty")) {
				brwarranty = true;
			}
			else if (qName.equalsIgnoreCase("category")) {
				bcategory = true;
			} 
			
			else if (qName.equalsIgnoreCase("retailer")) {
				bretailer = true;
			}

			else if (qName.equalsIgnoreCase("retailerZip")) {
				bretailerZip = true;
			}

			else if (qName.equalsIgnoreCase("retailerCity")) {
				bretailerCity = true;
			}
			else if (qName.equalsIgnoreCase("retailerState")) {
				bretailerState = true;
			}
			else if (qName.equalsIgnoreCase("productOnSale")) {
				bproductOnSale = true;
			}
			else if (qName.equalsIgnoreCase("manufacturerName")) {
				bmanufacturerName = true;
			}
			
			if(qName.equalsIgnoreCase("accessory"))
			{
				bacc = true;
				acc = new Accessory();
			}
			
			if(bacc)
			{
				if (qName.equalsIgnoreCase("accname")) {
					baccname = true;
				} else if (qName.equalsIgnoreCase("accprice")) {
					baccprice = true;
				} else if (qName.equalsIgnoreCase("accdiscount")) {
					baccdiscount = true;
				}

			}
		}
		
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("product")) {
			
			hpCatalog.put(product.getName(), product);
			bproduct = false;
			
		}
		if (qName.equalsIgnoreCase("accessory")) {
			//System.out.println("END of acc");
			
			accessoryMap.put(acc.getName(), acc);
			
			product.setAccessories(accessoryMap);
			accessoryMap = new HashMap<>();
			bacc = false;
		}
	
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
			if (bname) {
				product.setName(new String(ch, start, length));
				bname = false;
			}
			
			else if (bretailer) {
				product.setRetailer(new String(ch, start, length));
				bretailer = false;
			}
			
			else if (bdiscount) {
				product.setDiscount(Double.parseDouble(new String(ch, start, length)));
				bdiscount = false;
			}
			else if (brdiscount) {
				product.setRdiscount(Double.parseDouble(new String(ch, start, length)));
				brdiscount = false;
			}
			else if (brwarranty) {
				product.setRwarranty(Double.parseDouble(new String(ch, start, length)));
				brwarranty = false;
			}
			else if (bcategory) {
				product.setCategory(Category.fromString(new String(ch, start, length)));
				bcategory = false;
			}
			
			else if (bretailerZip) {
				product.setRetailerZip(Integer.parseInt(new String(ch, start, length)));
				bretailerZip = false;
			}
			else if (bretailerCity) {
				product.setRetailerCity(new String(ch, start, length));
				bretailerCity = false;
			}
			else if (bretailerState) {
				product.setRetailerState(new String(ch, start, length));
				bretailerState = false;
			}
			else if (bproductOnSale) {
				product.setProductOnSale(YesNo.fromString(new String(ch, start, length)));
				bproductOnSale = false;
			}
			else if (bmanufacturerName) {
				product.setManufacturerName(new String(ch, start, length));
				bmanufacturerName = false;
			}
			
			else if (bprice) {
				product.setPrice(Double.parseDouble(new String(ch, start, length)));
				bprice = false;
			}
			
			else if (baccname) {
				acc.setName(new String(ch, start, length));
				baccname = false;
			}	else if (baccprice) {
				acc.setPrice(Double.parseDouble(new String(ch, start, length)));
				baccprice = false;
			}else if (baccdiscount) {
				acc.setDiscount(Double.parseDouble(new String(ch, start, length)));
				baccdiscount = false;
			}
		
	}
	
}

