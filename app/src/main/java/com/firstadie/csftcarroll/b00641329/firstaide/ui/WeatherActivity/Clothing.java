package com.firstadie.csftcarroll.b00641329.firstaide.ui.WeatherActivity;

import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.TextLayoutUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tigh on 12/11/17.
 */

public enum Clothing {

    TSHIRT(R.drawable.ic_tshirt, 1),
    SHORTS(R.drawable.ic_shorts, 1),
    SUNGLASSES(R.drawable.ic_sunglasses, 1),
    JUMPER(R.drawable.ic_jumper, 2, 3, 4),
    JEANS(R.drawable.ic_jeans, 2, 3, 4),
    HOODIE(R.drawable.ic_hoodie, 2, 3, 4),
    CARDIGAN(R.drawable.ic_cardigan, 2, 3, 4),
    COAT(R.drawable.ic_coat, 3, 9, 10, 11, 13, 50),
    UMBRELLA(R.drawable.ic_umbrella, 9, 10, 11, 13),
    WELLY_BOOTS(R.drawable.ic_welly_boots, 9, 10, 11),
    GLOVES(R.drawable.ic_gloves, 3, 9, 10, 11, 13, 50),
    SCARF(R.drawable.ic_scarf, 3, 9, 10, 11, 13, 50),
    TORCH(R.drawable.ic_torch, 50);

    private int mIcon;
    private String[] mWeatherCodes;

    Clothing(int icon, int... weatherCodes) {
        mIcon = icon;

        mWeatherCodes = new String[weatherCodes.length * 2];
        for(int i = 0; i < weatherCodes.length; i++) {
            int j = i * 2;

            if(weatherCodes[i] < 10) {
                mWeatherCodes[j] = "0" + weatherCodes[i] + "d";
                mWeatherCodes[j + 1] = "0" + weatherCodes[i] + "n";
            } else {
                mWeatherCodes[j] = weatherCodes[i] + "d";
                mWeatherCodes[j + 1] = weatherCodes[i] + "n";
            }
        }
    }

    public static List<Clothing> forCode(String weatherCode) {
        List<Clothing> clothes = new ArrayList<>();

        for(Clothing item : Clothing.values()) {
            for(String itemCode : item.mWeatherCodes) {
                if(itemCode.equals(weatherCode)) {
                    clothes.add(item);
                }
            }
        }
        return clothes;
    }

    public String getTitle() {
        return TextLayoutUtils.toTitleCase(name().split("_"));
    }

    public int getIcon() {
        return mIcon;
    }
}
