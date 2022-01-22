package se.quedro.salesdatareader;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.quedro.dto.SaleObject;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SaleObjectDataHelperTest {

    @Test
    public void testReadFilesAndPopulateSaleObjectList_shouldReturnEmptySaleObjectList_whenUnsupportedFileExtension() {
        List<String> files = new ArrayList<>();
        files.add("abc.txt");

        SaleObjectDataHelper saleObjectDataHelper = new SaleObjectDataHelper();
        List<SaleObject> saleObjects = saleObjectDataHelper.readFilesAndPopulateSaleObjectList(files);

        Assert.assertNotNull(saleObjects);
        Assert.assertEquals(saleObjects.size(), 0);
    }

    @Test
    public void testReadFilesAndPopulateSaleObjectList_shouldReturnNonEmptySaleObjectList_whenSupportedFileExtension() throws URISyntaxException {
        List<String> files = new ArrayList<>();

        ClassLoader classLoader = this.getClass().getClassLoader();
        URL resource = classLoader.getResource("SaleObjects.xml");
        if (resource != null) {
            File file = new File(resource.toURI());
            files.add(file.getAbsolutePath());
        }

        SaleObjectDataHelper saleObjectDataHelper = new SaleObjectDataHelper();
        List<SaleObject> saleObjects = saleObjectDataHelper.readFilesAndPopulateSaleObjectList(files);

        Assert.assertNotNull(saleObjects);
        Assert.assertEquals(saleObjects.size(), 4);
    }
}
