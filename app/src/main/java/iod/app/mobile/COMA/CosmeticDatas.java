package iod.app.mobile.COMA;

/**
 * Created by dnjsd on 2016-12-02.
 */
public class CosmeticDatas {
    private String cosmeticName;
    private String cosmeticBrand;
    private int cosmeticID;
    private int cosmeticVolume;
    private String cosmeticType;
    private String cosmeticImageSrc;
    private String[] cosmeticTypes = {"스킨케어","클렌징/필링","마스크/팩","썬케어","베이스 메이크업","아이 메이크업","립 메이크업"
    , "바디","헤어","네일","향수","기타"};

    public CosmeticDatas(String cosmeticName, String cosmeticBrand, int cosmeticID) {
        this.cosmeticName = cosmeticName;
        this.cosmeticBrand = cosmeticBrand;
        this.cosmeticID = cosmeticID;
    }

    public CosmeticDatas(String cosmeticName, String cosmeticBrand, int cosmeticID, int cosmeticVolume, String cosmeticType) {
        this.cosmeticName = cosmeticName;
        this.cosmeticBrand = cosmeticBrand;
        this.cosmeticID = cosmeticID;
        this.cosmeticVolume = cosmeticVolume;
        this.cosmeticType = cosmeticType;
    }

    public String getCosmeticName() {
        return this.cosmeticName;
    }

    public String getCosmeticBrand() {
        return this.cosmeticBrand;
    }

    public int getCosmeticID() {
        return this.cosmeticID;
    }

    public int getCosmeticVolume(){
        return this.cosmeticVolume;
    }

    public String getCosmeticType() {
        return this.cosmeticType;
    }

    public void setCosmeticImageSrc(String src) {
        this.cosmeticImageSrc = src;
    }

    public String getCosmeticImageSrc() {
        return cosmeticImageSrc;
    }

    public int getCosmeticTypeIndex() {
        for(int i = 0; i < cosmeticTypes.length; i++) {
            if(this.cosmeticType.equals(cosmeticTypes[i])) {
                return i;
            }
        }
        return 11;
    }

}