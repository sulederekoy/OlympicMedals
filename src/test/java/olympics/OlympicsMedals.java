package olympics;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertNotSame;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
/*
 * Either omit the 6th table row, then select all descendants:

/html/body/table/tbody/tr[position() != 6]//*
or only select all table rows that are not at position 1 and have an attribute (and then select their descendants):

/html/body/table/tbody/tr[position() = 1 or @*]//*
or to be more specific, also check the attribute name:

/html/body/table/tbody/tr[position() = 1 or @height or @class]//*
 */

@Test
public class OlympicsMedals {
	WebDriver driver;
	Map<String, String> medals;

	@BeforeClass
	public void setUp() {
		// System.out.println("setting up WebDriver before class");
		WebDriverManager.chromedriver().setup();

	}

	@BeforeMethod
	public void beforeMethod() {
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	@Test
	public void sortTest() {
		driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
		List<WebElement> ranks = driver.findElements(By.xpath(
				"//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr [position() != 11]/td[1]"));
		// *[@id="mw-content-text"]/div/table[8]/tbody/tr[9]/td[1]
		List<WebElement> countriesBeforeClick = driver.findElements(
				By.xpath("//table[@class='wikitable sortable plainrowheaders " + "jquery-tablesorter']/tbody/tr/th"));
		List<String> countriesBefore = new ArrayList();
		for (WebElement each : countriesBeforeClick) {
			String country = each.getText();
			// System.out.println(country);
			countriesBefore.add(country);
		}
		List<Integer> ranksList = new ArrayList();
		for (int i = 0; i < ranks.size(); i++) {
			int eachRank = Integer.parseInt(ranks.get(i).getText());
			ranksList.add(eachRank);
			// System.out.println(eachRank);

		}
		// verify ranks are sorted in ascending order
		for (int i = 0; i < ranksList.size() - 1; i++) {
			assertTrue(ranksList.get(i) < ranksList.get(i + 1));
		}

		// click on NOC
		driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/thead/tr/th[2]")).click();
		;

		List<WebElement> countriesList = driver
				.findElements(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr/th/a"));
		List<String> countries = new ArrayList();
		for (WebElement each : countriesList) {
			String country = each.getText();
			// System.out.println(country);
			countries.add(country);
		}
		for (int i = 0; i < countries.size() - 1; i++) {
			assertTrue(countries.get(i).compareTo(countries.get(i + 1)) < 0);
		}

		List<WebElement> ranks2 = driver.findElements(
				By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr/td[1]"));
		List<Integer> ranksList2 = new ArrayList();
		for (WebElement eachrank : ranks2) {
			int eachRank2 = Integer.parseInt(eachrank.getText());
			ranksList2.add(eachRank2);
			// System.out.println(eachRank2);
		}
		for (int i = 0; i < ranksList2.size() - 1; i++) {
			// assertTrue(ranksList.get(i) < ranksList.get(i + 1));
			assertNotSame(ranksList2, ranksList);
		}

	}

	@Test
	public void theMost() throws InterruptedException {
		driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
		Thread.sleep(1000);
		String goldCountry = theMostGold(driver);
		System.out.println("country with the most gold medal: " + goldCountry);
		assertEquals(goldCountry, "United States");

		Thread.sleep(1000);
		String silverCountry = theMostSilver(driver);
		System.out.println("country with the most silver medal: " + silverCountry);
		assertEquals(silverCountry, "United States");

		Thread.sleep(1000);
		String bronzeCountry = theMostBronze(driver);
		System.out.println("country with the most bronze medal: " + bronzeCountry);
		assertEquals(bronzeCountry, "United States");

		Thread.sleep(1000);
		String theMostCountry = theMostMedals(driver);
		System.out.println("country with the most medals: " + theMostCountry);
		assertEquals(bronzeCountry, "United States");

	}

	// public void sortDescending(String link) {
	//
	// String actual = "Sort descending";
	// String expected = driver.findElement(By.xpath(link)).getAttribute("title");

	// assertEquals(actual, expected);

	// }

	public String theMostGold(WebDriver driver) {
		// driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
		WebElement gold = driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/thead/tr/th[3]"));

		if (gold.getAttribute("title").equals("Sort descending"))
			gold.click();
		// driver.findElement(By.xpath("/table[@class=‘wikitable sortable
		// plainrowheaders jquery-tablesorter’]/tbody/tr[1]/th/a"));
		// String goldCountry=driver.findElement(By.partialLinkText("United
		// States")).getText();
		String goldCountry = driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr[1]/th/a"))
				.getText();
		return goldCountry;
	}

	public String theMostSilver(WebDriver driver) {

		WebElement silver = driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/thead/tr/th[4]"));

		if (silver.getAttribute("title").equals("Sort descending"))
			silver.click();
		// driver.findElement(By.xpath("/table[@class=‘wikitable sortable
		// plainrowheaders jquery-tablesorter’]/tbody/tr[1]/th/a"));
		String silverCountry = driver
				.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr[1]/th/a")).getText();
		return silverCountry;
	}

	public String theMostBronze(WebDriver driver) {
		// driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
		WebElement bronze = driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/thead/tr/th[5]"));

		if (bronze.getAttribute("title").equals("Sort descending"))
			bronze.click();
		// driver.findElement(By.xpath("/table[@class=‘wikitable sortable
		// plainrowheaders jquery-tablesorter’]/tbody/tr[1]/th/a"));
		String bronzeCountry = driver
				.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr[1]/th/a")).getText();
		return bronzeCountry;
	}

	public String theMostMedals(WebDriver driver) {
		// driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
		WebElement total = driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/thead/tr/th[6]"));

		if (total.getAttribute("title").equals("Sort descending"))
			total.click();
		// driver.findElement(By.xpath("/table[@class=‘wikitable sortable
		// plainrowheaders jquery-tablesorter’]/tbody/tr[1]/th/a"));
		String mostCountry = driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr[1]/th/a"))
				.getText();
		return mostCountry;
	}

	@Test
	public void countryByMedal() {
		driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
		List<String> expected = Arrays.asList("China", "France");
		List<String> silver18 = silver18();
		// System.out.println(silver18);
		assertEquals(silver18, expected);

	}

	public List<String> silver18() {
		List<String> silver18Countries = new ArrayList();

		for (int i = 1; i <= 10; i++) {
			WebElement we = driver
					.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr[" + i + "]/td[3]"));
			if (we.getText().equals("18")) {
				silver18Countries.add(driver
						.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr[" + i + "]/th/a"))
						.getText());
			}
		}
		return silver18Countries;
	}

	@Test
	public void getIndex() {
		driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
		System.out.println(getRowColumn("Japan"));
	}

	public List<Integer> getRowColumn(String countryName) {
		List<Integer> rowColumn = new ArrayList();

		for (int i = 1; i <= 10; i++) {
			WebElement Names = driver
					.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr[" + i + "]/th/a"));
			if (Names.getText().equals(countryName)) {
				rowColumn.add(i);
			}
		}
		rowColumn.add(2);
		return rowColumn;

	}

	@Test
	public void getSum() {
		driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
		List<String> expected = Arrays.asList("Italy", "Australia");
		List<String> total18 = total18();
		System.out.println(total18);
		// assertEquals(total18, expected);
	}

	public List<String> total18() {
		List<String> bronze18Countries = new ArrayList();
		String path = "//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr/td[4]";
		List<WebElement> a = driver.findElements(By.xpath(path));
		for (int i = 1; i < 10; i++) {

			for (int j = i + 1; j <= 10; j++) {

				// List<WebElement> b=driver.findElements(By.xpath(path));
				if (Integer.parseInt(a.get(i).getText()) + Integer.parseInt(a.get(j).getText()) == 18) {
					System.out.println(a.get(i).getText() + "," + a.get(j).getText());
					bronze18Countries.add(driver
							.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr[" + i + "]/th/a"))
							.getText());
					bronze18Countries.add(driver
							.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr[" + j + "]/th/a"))
							.getText());
				}
			}
		}
		return bronze18Countries;
	}

	@AfterMethod
	public void tearDown() {// throws InterruptedException {
		// Thread.sleep(2000);
		driver.close();
	}

}
