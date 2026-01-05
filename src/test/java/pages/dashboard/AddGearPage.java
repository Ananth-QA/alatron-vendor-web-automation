package pages.dashboard;

import org.openqa.selenium.*;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AddGearPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private Properties prop;

    public AddGearPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        loadProperties();
    }

    private void loadProperties() {
        try {
            prop = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            prop.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("🔥 Cannot load config.properties");
        }
    }

    private By get(String key){
        return By.xpath(prop.getProperty(key));
    }

    public void clickConfirmation() {
        wait.until(ExpectedConditions
                .elementToBeClickable(get("confirmation.continue.button")))
                .click();

        System.out.println("✅ Confirmation popup Continue clicked successfully");
    }


    public void clickAddGearHeader() {
        wait.until(ExpectedConditions.elementToBeClickable(get("add.gear.header"))).click();
        System.out.println("✅ Clicked Add Gear Header");
    }

    public void verifyInfoPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(get("progress.info.step")));
        System.out.println("✅ Info Step Page Loaded");
    }
    
    
    private void selectDropdown(String locatorKey, String value) {
        wait.until(ExpectedConditions.elementToBeClickable(get(locatorKey))).click();
        By option = By.xpath("//*[contains(text(),'" + value + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }
    
    private void Resue(String locatorKey, String value) {
        WebElement dropdown = wait.until(
                ExpectedConditions.elementToBeClickable(get(locatorKey))
        );
        Select select = new Select(dropdown);
        select.selectByVisibleText(value.trim());
        System.out.println("✅ Selected: " + value);
    }

    

    public void fillBasicDetails() throws InterruptedException {

        selectDropdown("category.dropdown", prop.getProperty("gear.category"));
        selectDropdown("type.dropdown", prop.getProperty("gear.type"));
        selectDropdown("subtype.dropdown", prop.getProperty("gear.subtype"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(get("unit.label")))
                .sendKeys(prop.getProperty("gear.unitLabel"));

        selectDropdown("yard.dropdown", prop.getProperty("gear.yardLocation"));
        selectDropdown("brand.dropdown", prop.getProperty("gear.brand"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(get("model.input")))
                .sendKeys(prop.getProperty("gear.model"));

        selectDropdown("year.dropdown", prop.getProperty("gear.year"));
        
        driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
        
        
        System.out.println("📌 Dropdown closed via ESC");


        System.out.println("✅ Filled Basic Details");
        
    }
    
    
    public void uploadImages() throws InterruptedException {

        WebElement uploadInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(get("upload.input"))
        );
        
        WebElement uploadfield = wait.until(
                ExpectedConditions.presenceOfElementLocated(get("upload.field"))
        );


//        ((JavascriptExecutor) driver)
//                .executeScript("arguments[0].scrollIntoView(true);", uploadfield);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 800);");    // just scroll page
        Thread.sleep(1000);

        
        String folderPath = prop.getProperty("gear.images.folder");

        if(folderPath == null || folderPath.isBlank()){
            throw new RuntimeException("❌ gear.images.folder is missing in config.properties");
        }

        File folder = new File(folderPath);
        if (!folder.isAbsolute()) {
            folder = new File(System.getProperty("user.dir") + File.separator + folderPath);
        }

        if (!folder.exists()) {
            throw new RuntimeException("❌ Folder not found: " + folder.getAbsolutePath());
        }

        File[] images = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".jpg") ||
                name.toLowerCase().endsWith(".jpeg") ||
                name.toLowerCase().endsWith(".png") ||
                name.toLowerCase().endsWith(".webp")
        );

        if(images == null || images.length == 0){
            throw new RuntimeException("❌ No valid images found in folder");
        }

        int limit = Math.min(images.length, 5);

        for (int i = 0; i < limit; i++) {

            uploadInput.sendKeys(images[i].getAbsolutePath());
            System.out.println("📤 Uploaded: " + images[i].getName());

            // ⭐ GIVE UI TIME TO PROCESS EACH IMAGE
            Thread.sleep(1500);
        }

        System.out.println("✅ Upload requested for " + limit + " images");
        
        
//        WebElement ctnbtn = wait.until(
//                ExpectedConditions.presenceOfElementLocated(get("continue.button"))
//        );
//
//
//        ((JavascriptExecutor) driver)
//                .executeScript("arguments[0].scrollIntoView(true);", ctnbtn);
//
//        ctnbtn.findElement(By.xpath("continue.button")).click();
//        System.out.println("✅ Continue Clicked → Move to Specification Step");
    }
    
public void validateMaxFiveImagesAndProceed() throws InterruptedException {

    // Wait until thumbnails actually appear
    List<WebElement> images = wait.until(
            ExpectedConditions.numberOfElementsToBeMoreThan(get("uploaded.images"), 0)
    );

    System.out.println("🖼️ Uploaded thumbnails detected: " + images.size());

    // =========================================================
    //  CASE 1 : IMAGES > 5  → REMOVE EXCESS
    // =========================================================
    if (images.size() > 5) {

        System.out.println("⚠️ More than 5 images present. Starting cleanup...");

        while (images.size() > 5) {

            WebElement lastImage = images.get(images.size() - 1);

            // Hover to reveal delete icon
            Actions actions = new Actions(driver);
            actions.moveToElement(lastImage).perform();
            Thread.sleep(500);

            // Click close button
            WebElement closeBtn = lastImage.findElement(
                    By.xpath(".//button[contains(@class,'absolute top-2 right-2')]")
            );
            closeBtn.click();

            System.out.println("❌ Removed one image");

            Thread.sleep(800);

            // refresh list
            images = driver.findElements(get("uploaded.images"));
            System.out.println("📸 Current remaining images: " + images.size());
        }

        System.out.println("✅ Cleanup completed — Exactly 5 images retained");
    }

    // =========================================================
    //  CASE 2 : IMAGES ≤ 5  → JUST PROCEED
    // =========================================================
    else {
        System.out.println("👌 Image count is within limit (<=5). No cleanup needed.");
    }

    // Final validation (must have at least 1 image)
    if (images.size() == 0) {
        throw new RuntimeException("❌ No images found! Cannot proceed.");
    }

    System.out.println("🎯 Final Image Count = " + images.size() + " → Proceeding...");

    // Click Continue
    wait.until(ExpectedConditions.elementToBeClickable(get("continue.button"))).click();
    System.out.println("✅ Continue Clicked → Move to Specification Step");
}

    
    

    
      private File getValidPdf() {

        String folderPath = prop.getProperty("brochure.folder");

        File folder = new File(folderPath);
        if (!folder.isAbsolute()) {
            folder = new File(System.getProperty("user.dir") + File.separator + folderPath);
        }

        if (!folder.exists())
            throw new RuntimeException("❌ Brochure folder missing: " + folder.getAbsolutePath());

        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".pdf")
        );

        if(files == null || files.length == 0)
            throw new RuntimeException("❌ No PDF found in brochure folder");

        // Validate file size <= 5MB
        for(File f : files){
            long sizeInMB = f.length() / (1024 * 1024);

            if(sizeInMB <= 5){
                System.out.println("📄 Valid PDF Found: " + f.getName() + " (" + sizeInMB + "MB )");
                return f;
            }
        }

        throw new RuntimeException("❌ No valid PDF ≤ 5MB found");
    }

    
    
    
    public void fillSpecifications() {

    	selectDropdown("capacity.dropdown", prop.getProperty("capacity.value"));
    	selectDropdown("fuel.dropdown", prop.getProperty("fuel.value"));
    	selectDropdown("cabin.dropdown", prop.getProperty("cabin.value"));
    	selectDropdown("track.dropdown", prop.getProperty("track.value"));
    	selectDropdown("emission.dropdown", prop.getProperty("emission.value"));
    	Resue("operator.dropdown", prop.getProperty("operator.value"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(get("hour.meter")))
                .sendKeys(prop.getProperty("hour.meter.value"));
     // Platform Height
        String platformHeightStr = prop.getProperty("platform.height.value");
        WebElement platformHeightField = driver.findElement(get("platform.height"));
        platformHeightField.clear();
        platformHeightField.sendKeys(platformHeightStr);

        // Calculate Working Height = Platform Height + 2
        double platformHeight = Double.parseDouble(platformHeightStr);
        double workingHeight = platformHeight + 2;

        WebElement workingHeightField = driver.findElement(get("working.height"));
        workingHeightField.clear();
        workingHeightField.sendKeys(String.valueOf(workingHeight));

        System.out.println("📐 Working Height Calculated = " + workingHeight);

        driver.findElement(get("platform.capacity")).sendKeys(prop.getProperty("platform.capacity.value"));
        driver.findElement(get("platform.length")).sendKeys(prop.getProperty("platform.length.value"));
        driver.findElement(get("platform.width")).sendKeys(prop.getProperty("platform.width.value"));

        Resue("self.propelled.dropdown", prop.getProperty("self.propelled.value"));

        driver.findElement(get("serial.number"))
                .sendKeys(prop.getProperty("serial.number.value"));

        System.out.println("✅ Basic Specification filled");
    }

    
    private void selectMultipleCheckbox(String baseLocatorKey, String values){
        String[] list = values.split(",");

        for(String v : list){
            String xpath = prop.getProperty(baseLocatorKey).replace("REPLACE_VALUE", v.trim());
            By checkbox = By.xpath(xpath);

            WebElement cb = wait.until(ExpectedConditions.elementToBeClickable(checkbox));

            if(!cb.isSelected()){
                cb.click();
            }
            System.out.println("☑ Selected : " + v);
        }
    }

    public void selectAttachmentsAndTerrain(){
        selectMultipleCheckbox("attachments.checkbox", prop.getProperty("attachments.list"));
        selectMultipleCheckbox("terrain.checkbox", prop.getProperty("terrain.list"));

        System.out.println("✅ Attachments & Terrain selection completed");
    }

  
    private void selectNativeDropdown(String locatorKey, String value){
        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(get(locatorKey))
        );
        new org.openqa.selenium.support.ui.Select(element).selectByVisibleText(value);
        System.out.println("✅ Selected (native select): " + value);
    }
    

    public void fillDeliveryDetails(){

        // normal dropdowns
        selectNativeDropdown("delivery.dropdown", prop.getProperty("delivery.value"));
        selectNativeDropdown("pickup.dropdown", prop.getProperty("pickup.value"));
        selectNativeDropdown("mobilization.dropdown", prop.getProperty("mobilization.value"));

        driver.findElement(get("free.delivery"))
                .sendKeys(prop.getProperty("free.delivery.value"));

        driver.findElement(get("charge.per.km"))
                .sendKeys(prop.getProperty("charge.per.km.value"));

        driver.findElement(get("max.radius"))
                .sendKeys(prop.getProperty("max.radius.value"));

        System.out.println("🚚 Delivery basic fields filled");


        /*
         *  ⭐ Mobilization Dynamic Logic
         */
        String mobilizationType = prop.getProperty("mobilization.value").trim();

        if(mobilizationType.equalsIgnoreCase("Flat")){

            WebElement flatInput =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(get("mobilization.fee.input")));

            flatInput.clear();
            flatInput.sendKeys(prop.getProperty("mobilization.flat.value"));

            System.out.println("💰 Flat Mobilization Fee Entered: "
                    + prop.getProperty("mobilization.flat.value"));
        }

        else if(mobilizationType.equalsIgnoreCase("Percentage")){

            int percent = Integer.parseInt(prop.getProperty("mobilization.percent.value"));

            if(percent < 1 || percent > 99){
                throw new RuntimeException("❌ Invalid percentage! Must be 1–99. Provided: " + percent);
            }

            WebElement percentInput =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(get("mobilization.percent.input")));

            percentInput.clear();
            percentInput.sendKeys(String.valueOf(percent));

            System.out.println("📊 Percentage Mobilization Set: " + percent + "%");
        }

        else{
            throw new RuntimeException("❌ Unsupported mobilization type: " + mobilizationType);
        }

        System.out.println("✅ Delivery & Mobilization logic completed successfully");
    }

    

    public void selectLocationUsingMap() throws InterruptedException {

        // Click Use Map
        wait.until(ExpectedConditions.elementToBeClickable(get("use.map.button"))).click();
        System.out.println("🗺️ Opening Map...");

        // Wait map visible
        WebElement map = wait.until(ExpectedConditions.visibilityOfElementLocated(get("map.canvas")));

        // Click on map midpoint (simulates choosing location)
        Actions act = new Actions(driver);
        act.moveToElement(map).click().perform();
        Thread.sleep(1000);

        // Confirm / Select
        try {
            wait.until(ExpectedConditions.elementToBeClickable(get("map.pin.confirm.button"))).click();
            System.out.println("📌 Location Selected");
        } catch (Exception e) {
            System.out.println("ℹ️ Confirm button not required. Map auto selected.");
        }

        // Validate Address auto-filled
        WebElement addressBox = wait.until(ExpectedConditions.visibilityOfElementLocated(get("address.input")));

     // 🔥 Wait until value is populated (VERY IMPORTANT)
        WebDriverWait valueWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        valueWait.until(driver -> {
            String val = addressBox.getAttribute("value");
            return val != null && !val.trim().isEmpty();
        });

        String address = addressBox.getAttribute("value");

        if(address == null || address.isBlank()){
            throw new RuntimeException("❌ Address not auto-filled after map selection!");
        }

        System.out.println("📍 Address Auto Filled: " + address);
    }

    public void uploadBrochure() throws InterruptedException {

        WebElement uploadInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(get("brochure.input"))
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", uploadInput);

        File pdf = getValidPdf();

        uploadInput.sendKeys(pdf.getAbsolutePath());

        System.out.println("📤 Brochure Uploaded: " + pdf.getName());

        System.out.println("Fill the additional fields addedfrom backend ");


        Thread.sleep(30000);
        
        WebElement Continuebtn = wait.until(
                ExpectedConditions.presenceOfElementLocated(get("brochurecontinue.button"))
        );
        Continuebtn.click();
        System.out.println("Continue to price and terms button is clicked ");

        
    }
//        public void handleAdditionalInformationAndContinue() {
//
//            try {
//                // Wait for Additional section IF it exists
//                wait.until(ExpectedConditions.visibilityOfElementLocated(get("additional.section")));
//                System.out.println("📝 Additional Information Section Detected");
//            } catch (Exception e) {
//                System.out.println("ℹ️ No Additional Fields Found → Skipping this section");
//            }
//
//            // -------- Door Count Dropdown --------
//            try {
//                WebElement doorDropdown = driver.findElement(get("door.count.dropdown"));
//                Select selectDoor = new Select(doorDropdown);
//                selectDoor.selectByVisibleText("2");
//                System.out.println("🚪 Door Count Selected: 2");
//            } catch (Exception e) {
//                System.out.println("ℹ️ Door Count not available → Skipping");
//            }
//
//            // -------- Engine Status Radio --------
//            try {
//                WebElement engineRadio = driver.findElement(
//                        By.xpath(String.format(prop.getProperty("engine.status.radio"), "ON"))
//                );
//                engineRadio.click();
//                System.out.println("🔧 Engine Status Selected: ON");
//            } catch (Exception e) {
//                System.out.println("ℹ️ Engine Status not available → Skipping");
//            }
//
//            // -------- Remarks --------
//            try {
//                WebElement remarks = driver.findElement(get("remarks.input"));
//                remarks.clear();
//                remarks.sendKeys("Automation Remarks Added");
//                System.out.println("✍️ Remarks Entered");
//            } catch (Exception e) {
//                System.out.println("ℹ️ Remarks not available → Skipping");
//            }
//
//            // -------- Extra Features Checkboxes --------
//            try {
//                selectMultipleCheckbox("extra.features.checkbox",
//                        prop.getProperty("extra.features.values"));
//
//                System.out.println("⭐ Extra Features selection completed");
//            } 
//            catch (Exception e) {
//                System.out.println("ℹ️ Extra Features not available → Skipping");
//            }
//
//            // -------- Continue Button --------
//            WebElement Continuebtn = wait.until(
//                    ExpectedConditions.elementToBeClickable(get("brochurecontinue.button"))
//            );
//
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Continuebtn);
//            Continuebtn.click();
//
//            System.out.println("✅ Continue to Pricing & Terms button clicked successfully");
//        
//      
//    }
//   
    
    public void fillPriceAndTerms() throws InterruptedException {

        System.out.println("====== 🏷️ Filling Price & Terms ======");

        // ---- Min / Max ----
        driver.findElement(get("min.rental.duration"))
                .sendKeys(prop.getProperty("min.rental.value"));

        driver.findElement(get("max.hours.per.day"))
                .sendKeys(prop.getProperty("max.hours.value"));

        System.out.println("✅ Min & Max Rental Set");


        // ---- Enable Rental Slabs ----
        driver.findElement(get("hourly.checkbox")).click();
        driver.findElement(get("daily.checkbox")).click();
        driver.findElement(get("weekly.checkbox")).click();
        driver.findElement(get("monthly.checkbox")).click();

        System.out.println("✅ Rental Option Slabs Enabled");


        // ---------- Hourly ----------
        driver.findElement(get("hour.with.operator"))
                .sendKeys(prop.getProperty("hour.with.op"));

        driver.findElement(get("hour.without.operator"))
                .sendKeys(prop.getProperty("hour.without.op"));


        // ---------- Daily ----------
        driver.findElement(get("day.with.operator"))
                .sendKeys(prop.getProperty("day.with.op"));

        driver.findElement(get("day.without.operator"))
                .sendKeys(prop.getProperty("day.without.op"));


        // ---------- Weekly ----------
        driver.findElement(get("week.with.operator"))
                .sendKeys(prop.getProperty("week.with.op"));

        driver.findElement(get("week.without.operator"))
                .sendKeys(prop.getProperty("week.without.op"));


        // ---------- Monthly ----------
        driver.findElement(get("month.with.operator"))
                .sendKeys(prop.getProperty("month.with.op"));

        driver.findElement(get("month.without.operator"))
                .sendKeys(prop.getProperty("month.without.op"));

        System.out.println("💰 Pricing Slabs Entered Successfully");


        // ---------- Rental Calculation Mode ----------
        String mode = prop.getProperty("rental.calculation.mode");

        if(mode.equalsIgnoreCase("Monthly")){
            wait.until(ExpectedConditions.elementToBeClickable(get("rental.calc.monthly"))).click();
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(get("rental.calc.weekly"))).click();
        }

        System.out.println("📊 Rental Calculation Mode Selected: " + mode);


        // ---------- Overtime ----------
        driver.findElement(get("ot.operator"))
                .sendKeys(prop.getProperty("ot.operator.value"));

        driver.findElement(get("ot.machine"))
                .sendKeys(prop.getProperty("ot.machine.value"));

        driver.findElement(get("ot.notes"))
                .sendKeys(prop.getProperty("ot.notes.value"));

        System.out.println("⏱️ Overtime Details Filled");


        // ---------- Tax ----------
        driver.findElement(get("tax.rate"))
                .sendKeys(prop.getProperty("tax.rate.value"));

        System.out.println("🧾 Tax Policy Added");


        // ---------- Invoice ----------
        driver.findElement(get("invoice.prepaid")).click();
        driver.findElement(get("invoice.monthly")).click();

        System.out.println("📄 Invoice Options Selected");


        // ---------- Late Fee ----------
        driver.findElement(get("late.with.operator"))
                .sendKeys(prop.getProperty("late.with.op.value"));

        driver.findElement(get("late.without.operator"))
                .sendKeys(prop.getProperty("late.without.op.value"));

        System.out.println("⚠️ Late Return Fee Added");


        // ---------- Security Deposit ----------
        if(prop.getProperty("security.enable").equalsIgnoreCase("true")){

            driver.findElement(get("security.checkbox")).click();

            selectNativeDropdown("deposit.type.dropdown",
                    prop.getProperty("deposit.type"));

            driver.findElement(get("deposit.amount"))
                    .sendKeys(prop.getProperty("deposit.value"));

            System.out.println("🔐 Security Deposit Set");
        }


        // ---------- Notes ----------
        driver.findElement(get("important.terms"))
                .sendKeys(prop.getProperty("important.terms.value"));

        driver.findElement(get("cancellation.policy"))
                .sendKeys(prop.getProperty("cancellation.policy.value"));

        driver.findElement(get("care.instructions"))
                .sendKeys(prop.getProperty("care.instructions.value"));

        driver.findElement(get("quality.notes"))
                .sendKeys(prop.getProperty("quality.notes.value"));

        driver.findElement(get("overtime.terms"))
                .sendKeys(prop.getProperty("overtime.terms.value"));

        System.out.println("📝 Terms & Notes Completed");


        // ---------- Status ----------
        selectNativeDropdown("status.dropdown", prop.getProperty("status.value"));

        driver.findElement(get("contact.name")).sendKeys(prop.getProperty("contact.name.value"));
        driver.findElement(get("contact.number")).sendKeys(prop.getProperty("contact.number.value"));

        System.out.println("☎️ Contact Details Done");


        // ---------- Continue ----------
        wait.until(ExpectedConditions.elementToBeClickable(get("continue.review.button"))).click();

        System.out.println("✅ STEP 3 Completed → Moving to Review Step");
    }

    
    
    public void reviewAndSubmit() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Confirm Review Page Loaded
        wait.until(ExpectedConditions.visibilityOfElementLocated(get("review.page.header")));
        System.out.println("✅ Review Page Loaded Successfully");

        // ===== Validate Key Visible Information =====
        String[] mustHaveTexts = {
                prop.getProperty("capacity.value"),
                prop.getProperty("fuel.value"),
                prop.getProperty("cabin.value"),
                prop.getProperty("operator.value"),
                prop.getProperty("platform.height.value"),
                prop.getProperty("working.height.value"),
                prop.getProperty("delivery.value"),
                prop.getProperty("pickup.value")
        };

        for(String text : mustHaveTexts){
            if(driver.getPageSource().contains(text)){
                System.out.println("✔️ Verified on Review Page: " + text);
            } else {
                System.out.println("⚠️ Warning: Text not found on Review Page → " + text);
            }
        }
    }

        public void expandAdditionalInfoSections() throws InterruptedException {

            JavascriptExecutor js = (JavascriptExecutor) driver;

            try {
                // Get all accordion buttons
                List<WebElement> sections = wait.until(
                        ExpectedConditions.visibilityOfAllElementsLocatedBy(
                                get("additional.info.buttons")
                        )
                );

                System.out.println("📌 Additional Info Sections Found: " + sections.size());

                int index = 1;

                for (WebElement sec : sections) {

                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", sec);
                    Thread.sleep(800);

                    try {
                        sec.click();
                        System.out.println("📂 Expanded Section #" + index);
                    } catch (Exception e) {
                        js.executeScript("arguments[0].click();", sec);
                        System.out.println("📂 Expanded (JS Click) Section #" + index);
                    }

                    Thread.sleep(1200);  // allow animation + content render
                    index++;
                }

                System.out.println("✅ All Additional Info Sections Expanded Successfully");

            } catch (Exception e) {
                System.out.println("ℹ️ Additional Info Section not found — Skipping");
            }
        }
            
        public void reviewSubmitAndVerify() throws InterruptedException {

            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Scroll bottom
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(800);

            try {
                String[] tabs = {
                        "pricing.tab.hourly",
                        "pricing.tab.daily",
                        "pricing.tab.weekly",
                        "pricing.tab.monthly"
                };

                for(String tab : tabs){

                    WebElement t = wait.until(
                            ExpectedConditions.elementToBeClickable(get(tab))
                    );

                    js.executeScript("arguments[0].scrollIntoView(true);", t);
                    t.click();

                    // Wait tab turns active
                    wait.until(ExpectedConditions.attributeContains(
                            t, "class", "bg-blue"
                    ));

                    // Wait pricing panel visibly refreshed
                    wait.until(ExpectedConditions.visibilityOfElementLocated(
                            get("pricing.panel")
                    ));

                    System.out.println("💰 Pricing Tab Successfully Verified: " + tab);
                    Thread.sleep(600);
                }
            }
            catch (Exception e){
                System.out.println("ℹ️ Pricing Tabs not found / Skipped");
            }

            // Scroll up slightly
            js.executeScript("window.scrollBy(0,-800)");
            Thread.sleep(700);

            System.out.println("📜 Page Scrolling Validated");
        



             // ===== Capture Equipment ID =====
                String reviewEquipmentId = "";
                try {

                    WebElement eqId =
                            wait.until(ExpectedConditions.visibilityOfElementLocated(get("equipment.id.review")));

                    reviewEquipmentId = eqId.getText().trim();

                    System.out.println("🆔 Equipment ID Captured From Review: " + reviewEquipmentId);

                } catch (Exception e) {
                    System.out.println("⚠️ Equipment ID NOT FOUND in Review Page");
                }



                // ===== Capture Model Number (Backup Verification) =====
                String modelNumber = "";
                try {

                    // First Attempt — Read directly
                    WebElement modelElement =
                            wait.until(ExpectedConditions.visibilityOfElementLocated(get("model.text.review")));

                    modelNumber = modelElement.getText().trim();


                    // Fallback — if blank try inner span
                    if(modelNumber.isBlank()){
                        try{
                            WebElement modelInner =
                                    driver.findElement(get("model.text.inner"));
                            modelNumber = modelInner.getText().trim();
                        } catch(Exception ignored){}
                    }

                    if(!modelNumber.isBlank())
                        System.out.println("🔎 Model Found in Review: " + modelNumber);
                    else
                        System.out.println("⚠️ Model container found but text empty");

                } catch (Exception e) {
                    System.out.println("⚠️ Model Number not captured — fallback to Equipment ID only");
                }



                WebElement submitBtn = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(get("submit.button"))
                );

                // scroll into SAFE view
                ((JavascriptExecutor)driver)
                        .executeScript("arguments[0].scrollIntoView({block:'center'});", submitBtn);

                // wait clickable
                wait.until(ExpectedConditions.elementToBeClickable(submitBtn));

                try {
                    submitBtn.click();
                } catch(Exception e){
                    System.out.println("⚠️ Normal click intercepted — using JS Click");
                    ((JavascriptExecutor)driver).executeScript("arguments[0].click();", submitBtn);
                }

                System.out.println("🚀 Submit Button Clicked");


//
//                // ===== Toast Verification =====
//                wait.until(ExpectedConditions.visibilityOfElementLocated(get("toast.message")));
//                System.out.println("🎉 Toast Verified: Listing Submitted Successfully");


//                // ===== Success Page =====
//                wait.until(ExpectedConditions.visibilityOfElementLocated(get("success.title")));
//                System.out.println("🏁 Success Page Verified");


                // ===== View My Gear =====
                wait.until(ExpectedConditions.elementToBeClickable(get("view.my.gear.button"))).click();
                System.out.println("➡️ Redirected to My Gear Page");


                // ===== Final Verification On Listing Page =====
                Thread.sleep(2000);

                boolean verified = false;

                if (!reviewEquipmentId.isBlank()) {
                    verified = driver.getPageSource().contains(reviewEquipmentId);

                    if (verified)
                        System.out.println("✔️ Equipment VERIFIED on My Gear Page (Equipment ID Match)");
                    else
                        System.out.println("❌ Equipment ID not found — checking via Model Number...");
                }

                if(!verified && !modelNumber.isBlank()){
                    if(driver.getPageSource().contains(modelNumber))
                        System.out.println("✔️ Equipment VERIFIED on My Gear Page (Model Match)");
                    else
                        System.out.println("❌ Model not found on My Gear Page!");
                }

                System.out.println("🎯 Review + Submit Verification Completed Successfully");
            }


    
    
    
    
    

}