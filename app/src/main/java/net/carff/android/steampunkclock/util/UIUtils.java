package net.carff.android.steampunkclock.util;

import android.content.Context;
import java.util.Calendar;
import net.carff.android.steampunkclock.BuildConfig;
import net.carff.android.steampunkclock.R;
/* loaded from: classes.dex */
public class UIUtils {
    private static String TAG = "UIUtils";

    public static int getNumberResource(int number) {
        switch (number) {
            case 0:
                return R.drawable.zeroam;
            case 1:
                return R.drawable.oneam;
            case 2:
                return R.drawable.twoam;
            case 3:
                return R.drawable.threeam;
            case 4:
                return R.drawable.fouram;
            case 5:
                return R.drawable.fiveam;
            case 6:
                return R.drawable.sixam;
            case 7:
                return R.drawable.sevenam;
            case 8:
                return R.drawable.eightam;
            case 9:
                return R.drawable.nineam;
            default:
                return R.drawable.zeroam;
        }
    }

    public static String getDayOfWeek(int day) {
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "";
        }
    }

    public static boolean isGoogleTV(Context context) {
        return context.getPackageManager().hasSystemFeature("com.google.android.tv");
    }

    public static boolean isDay() {
        Calendar cal = Calendar.getInstance();
        if (cal.get(11) < 16 && cal.get(11) > 5) {
            return true;
        }
        return false;
    }

    public static int getWeatherIconResource(int weatherCode) {
        if (isDay()) {
            switch (weatherCode) {
                case 113:
                    return R.drawable.day_113;
                case 116:
                    return R.drawable.day_116;
                case 119:
                    return R.drawable.day_119;
                case 122:
                    return R.drawable.day_122;
                case 143:
                    return R.drawable.day_143;
                case 176:
                    return R.drawable.day_176;
                case 179:
                    return R.drawable.day_179;
                case 182:
                    return R.drawable.day_182;
                case 185:
                    return R.drawable.day_185;
                case 200:
                    return R.drawable.day_200;
                case 227:
                    return R.drawable.day_227;
                case 230:
                    return R.drawable.day_230;
                case 248:
                    return R.drawable.day_248;
                case 260:
                    return R.drawable.day_260;
                case 263:
                    return R.drawable.day_263;
                case 266:
                    return R.drawable.day_266;
                case 281:
                    return R.drawable.day_281;
                case 284:
                    return R.drawable.day_284;
                case 293:
                    return R.drawable.day_293;
                case 296:
                    return R.drawable.day_296;
                case 299:
                    return R.drawable.day_299;
                case 302:
                    return R.drawable.day_302;
                case 305:
                    return R.drawable.day_305;
                case 308:
                    return R.drawable.day_308;
                case 311:
                    return R.drawable.day_311;
                case 314:
                    return R.drawable.day_314;
                case 317:
                    return R.drawable.day_317;
                case 320:
                    return R.drawable.day_320;
                case 323:
                    return R.drawable.day_323;
                case 326:
                    return R.drawable.day_326;
                case 329:
                    return R.drawable.day_329;
                case 332:
                    return R.drawable.day_332;
                case 335:
                    return R.drawable.day_335;
                case 338:
                    return R.drawable.day_338;
                case 350:
                    return R.drawable.day_350;
                case 353:
                    return R.drawable.day_353;
                case 356:
                    return R.drawable.day_356;
                case 359:
                    return R.drawable.day_359;
                case 362:
                    return R.drawable.day_362;
                case 365:
                    return R.drawable.day_365;
                case 368:
                    return R.drawable.day_368;
                case 371:
                    return R.drawable.day_371;
                case 374:
                    return R.drawable.day_374;
                case 377:
                    return R.drawable.day_377;
                case 386:
                    return R.drawable.day_386;
                case 389:
                    return R.drawable.day_389;
                case 392:
                    return R.drawable.day_392;
                case 395:
                    return R.drawable.day_395;
                default:
                    return R.drawable.day_113;
            }
        }
        switch (weatherCode) {
            case 113:
                return R.drawable.night_113;
            case 116:
                return R.drawable.night_116;
            case 119:
                return R.drawable.night_119;
            case 122:
                return R.drawable.night_122;
            case 143:
                return R.drawable.night_143;
            case 176:
                return R.drawable.night_176;
            case 179:
                return R.drawable.night_179;
            case 182:
                return R.drawable.night_182;
            case 185:
                return R.drawable.night_185;
            case 200:
                return R.drawable.night_200;
            case 227:
                return R.drawable.night_227;
            case 230:
                return R.drawable.night_230;
            case 248:
                return R.drawable.night_248;
            case 260:
                return R.drawable.night_260;
            case 263:
                return R.drawable.night_263;
            case 266:
                return R.drawable.night_266;
            case 281:
                return R.drawable.night_281;
            case 284:
                return R.drawable.night_284;
            case 293:
                return R.drawable.night_293;
            case 296:
                return R.drawable.night_296;
            case 299:
                return R.drawable.night_299;
            case 302:
                return R.drawable.night_302;
            case 305:
                return R.drawable.night_305;
            case 308:
                return R.drawable.night_308;
            case 311:
                return R.drawable.night_311;
            case 314:
                return R.drawable.night_314;
            case 317:
                return R.drawable.night_317;
            case 320:
                return R.drawable.night_320;
            case 323:
                return R.drawable.night_323;
            case 326:
                return R.drawable.night_326;
            case 329:
                return R.drawable.night_329;
            case 332:
                return R.drawable.night_332;
            case 335:
                return R.drawable.night_335;
            case 338:
                return R.drawable.night_338;
            case 350:
                return R.drawable.night_350;
            case 353:
                return R.drawable.night_353;
            case 356:
                return R.drawable.night_356;
            case 359:
                return R.drawable.night_359;
            case 362:
                return R.drawable.night_362;
            case 365:
                return R.drawable.night_365;
            case 368:
                return R.drawable.night_368;
            case 371:
                return R.drawable.night_371;
            case 374:
                return R.drawable.night_374;
            case 377:
                return R.drawable.night_377;
            case 386:
                return R.drawable.night_386;
            case 389:
                return R.drawable.night_389;
            case 392:
                return R.drawable.night_392;
            case 395:
                return R.drawable.night_395;
            default:
                return R.drawable.day_113;
        }
    }
}
