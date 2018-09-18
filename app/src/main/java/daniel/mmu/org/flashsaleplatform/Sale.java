package daniel.mmu.org.flashsaleplatform;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Sale implements Serializable {

    private int saleId;
    private int shopId;
    private String saleTitle;
    private String originalPrice;
    private String salePrice;
    private String saleDescription;
    private String saleImage;

    public Sale() {

    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getSaleTitle() {
        return saleTitle;
    }

    public void setSaleTitle(String saleTitle) {
        this.saleTitle = saleTitle;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSaleDescription() {
        return saleDescription;
    }

    public void setSaleDescription(String saleDescription) {
        this.saleDescription = saleDescription;
    }

    public String getSaleImage() {
        return saleImage;
    }

    public void setSaleImage(String saleImage) {
        this.saleImage = saleImage;
    }

    @Override
    public String toString() {
        return "Sale Title: " + getSaleTitle() + System.getProperty("line.separator")
                + "Original Price: " + getOriginalPrice() + System.getProperty("line.separator")
                + "Sale Price: " + getSalePrice() + System.getProperty("line.separator")
                + "Sale Description: " + getSaleDescription();
    }
}

