package com.ontraport.mobileapp.utils;


import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import com.ontraport.mobileapp.R;

public class ThemeUtils {

    @DrawableRes
    public static int getIconByName(String name) {
        // @formatter:off
        switch (name) {
            case "airplane":              return R.drawable.ic_airplanemode_active_black_24dp;
            case "add-alert":             return R.drawable.ic_add_alert_black_24dp;
            case "alarm-clock":           return R.drawable.ic_access_alarm_black_24dp;
            case "badge":                 return R.drawable.ic_person_black_24dp;
            case "bang-burst":            return R.drawable.ic_person_black_24dp;
            case "basket":                return R.drawable.ic_person_black_24dp;
            case "birthday-cake":         return R.drawable.ic_cake_black_24dp;
            case "bookmark-2":            return R.drawable.ic_book_black_24dp;
            case "bookmark":              return R.drawable.ic_bookmark_black_24dp;
            case "briefcase-credit-card": return R.drawable.ic_person_black_24dp;
            case "briefcase-lay-button":  return R.drawable.ic_person_black_24dp;
            case "briefcase":             return R.drawable.ic_person_black_24dp;
            case "bug":                   return R.drawable.ic_bug_report_black_24dp;
            case "business-2":            return R.drawable.ic_person_black_24dp;
            case "business":              return R.drawable.ic_person_black_24dp;
            case "calendar":              return R.drawable.ic_person_black_24dp;
            case "camera":                return R.drawable.ic_camera_alt_black_24dp;
            case "carriage":              return R.drawable.ic_person_black_24dp;
            case "chair":                 return R.drawable.ic_person_black_24dp;
            case "circle-checkmark":      return R.drawable.ic_check_circle_black_24dp;
            case "circle-cloud":          return R.drawable.ic_cloud_circle_black_24dp;
            case "circle-face":           return R.drawable.ic_face_black_24dp;
            case "circle-star":           return R.drawable.ic_person_black_24dp;
            case "cloud":                 return R.drawable.ic_person_black_24dp;
            case "compass":               return R.drawable.ic_person_black_24dp;
            case "email-outline":         return R.drawable.ic_person_black_24dp;
            case "fingerprint":           return R.drawable.ic_person_black_24dp;
            case "fire":                  return R.drawable.ic_person_black_24dp;
            case "flower":                return R.drawable.ic_person_black_24dp;
            case "giftcard":              return R.drawable.ic_person_black_24dp;
            case "globe":                 return R.drawable.ic_person_black_24dp;
            case "hand-palm":             return R.drawable.ic_person_black_24dp;
            case "happyface":             return R.drawable.ic_person_black_24dp;
            case "headset":               return R.drawable.ic_person_black_24dp;
            case "interact":              return R.drawable.ic_person_black_24dp;
            case "keyboard":              return R.drawable.ic_person_black_24dp;
            case "laptop-arrow":          return R.drawable.ic_person_black_24dp;
            case "lcd-mobile":            return R.drawable.ic_person_black_24dp;
            case "lightbulb-outline":     return R.drawable.ic_person_black_24dp;
            case "lock-full":             return R.drawable.ic_person_black_24dp;
            case "lotus":                 return R.drawable.ic_person_black_24dp;
            case "mallet":                return R.drawable.ic_person_black_24dp;
            case "man-running":           return R.drawable.ic_person_black_24dp;
            case "man-tree":              return R.drawable.ic_person_black_24dp;
            case "metric-reversed":       return R.drawable.ic_person_black_24dp;
            case "mic-outline":           return R.drawable.ic_person_black_24dp;
            case "mic":                   return R.drawable.ic_person_black_24dp;
            case "mobile":                return R.drawable.ic_person_black_24dp;
            case "mortarboard-2":         return R.drawable.ic_person_black_24dp;
            case "motorcycle":            return R.drawable.ic_person_black_24dp;
            case "movie-clip-stars":      return R.drawable.ic_person_black_24dp;
            case "movie-reel":            return R.drawable.ic_person_black_24dp;
            case "new-comment":           return R.drawable.ic_person_black_24dp;
            case "open-book":             return R.drawable.ic_person_black_24dp;
            case "paw":                   return R.drawable.ic_pets_black_24dp;
            case "people-circle":         return R.drawable.ic_person_black_24dp;
            case "person-checkbox":       return R.drawable.ic_person_black_24dp;
            case "person-email":          return R.drawable.ic_person_black_24dp;
            case "person-phone":          return R.drawable.ic_person_black_24dp;
            case "phone-info":            return R.drawable.ic_person_black_24dp;
            case "phone-ring":            return R.drawable.ic_person_black_24dp;
            case "planet-earth":          return R.drawable.ic_person_black_24dp;
            case "plane-land":            return R.drawable.ic_person_black_24dp;
            case "pregnant":              return R.drawable.ic_pregnant_woman_black_24dp;
            case "propeller":             return R.drawable.ic_person_black_24dp;
            case "puzzle-piece":          return R.drawable.ic_person_black_24dp;
            case "radio":                 return R.drawable.ic_person_black_24dp;
            case "roman-house":           return R.drawable.ic_person_black_24dp;
            case "rowing":                return R.drawable.ic_person_black_24dp;
            case "scene-clip":            return R.drawable.ic_person_black_24dp;
            case "shield-checkmark":      return R.drawable.ic_person_black_24dp;
            case "signal":                return R.drawable.ic_person_black_24dp;
            case "snippet":               return R.drawable.ic_person_black_24dp;
            case "square-circle":         return R.drawable.ic_person_black_24dp;
            case "star":                  return R.drawable.ic_person_black_24dp;
            case "tag-heart":             return R.drawable.ic_loyalty_black_24dp;
            case "thumbs-up":             return R.drawable.ic_person_black_24dp;
            case "thumbsdown":            return R.drawable.ic_person_black_24dp;
            case "two-people":            return R.drawable.ic_supervisor_account_black_24dp;
            case "umbrella":              return R.drawable.ic_person_black_24dp;
            case "up-arrow-cloud":        return R.drawable.ic_person_black_24dp;
            case "videos-cricle":         return R.drawable.ic_person_black_24dp;
            case "weight-bench":          return R.drawable.ic_person_black_24dp;
            case "baby":                  return R.drawable.ic_person_black_24dp;
            case "beaker":                return R.drawable.ic_person_black_24dp;
            case "boat":                  return R.drawable.ic_person_black_24dp;
            case "boot":                  return R.drawable.ic_person_black_24dp;
            case "car":                   return R.drawable.ic_directions_car_black_24dp;
            case "castle":                return R.drawable.ic_castle_black_24dp;
            case "cat":                   return R.drawable.ic_person_black_24dp;
            case "dog":                   return R.drawable.ic_person_black_24dp;
            case "dollar-bill":           return R.drawable.ic_attach_money_black_24dp;
            case "hat":                   return R.drawable.ic_person_black_24dp;
            case "nuclear":               return R.drawable.ic_person_black_24dp;
            case "person-circle-slash":   return R.drawable.ic_person_black_24dp;
            case "ring":                  return R.drawable.ic_person_black_24dp;
            case "robot":                 return R.drawable.ic_person_black_24dp;
            case "store":                 return R.drawable.ic_person_black_24dp;
            case "teacher":               return R.drawable.ic_person_black_24dp;
            case "ticket":                return R.drawable.ic_person_black_24dp;
            case "document-3":            return R.drawable.ic_person_black_24dp;
            case "bubble":                return R.drawable.ic_person_black_24dp;
            case "house":                 return R.drawable.ic_person_black_24dp;
            case "plus":                  return R.drawable.ic_add_black_24dp;
            case "tag":                   return R.drawable.ic_tag_black_24dp;
            case "truck":                 return R.drawable.ic_person_black_24dp;
            default:                      return R.drawable.ic_person_black_24dp;
        }
        // @formatter:on
    }

    @ColorRes
    public static int getThemeByName(String name) {
        // @formatter:off
        switch (name) {
            case "ussr-theme-blue-800":             return R.color.blue_800;
            case "ussr-theme-dark-wizard":          return R.color.dark_wizard;
            case "ussr-theme-sherpa-blue":          return R.color.sherpa_blue;
            case "ussr-theme-tropical-rain-forest": return R.color.tropical_rain_forest;
            case "ussr-theme-ming":                 return R.color.ming;
            case "ussr-theme-thatch-green":         return R.color.thatch_green;
            case "ussr-theme-metallic-bronze":      return R.color.metallic_bronze;
            case "ussr-theme-chelsea-gem":          return R.color.chelsea_gem;
            case "ussr-theme-blackberry":           return R.color.blackberry;
            case "ussr-theme-finn":                 return R.color.finn;
            case "ussr-theme-rouge":                return R.color.rouge;
            case "ussr-theme-red-beech":            return R.color.red_beech;
            case "ussr-theme-fire":                 return R.color.fire;
            case "ussr-theme-trinidad":             return R.color.trinidad;
            case "ussr-theme-gulf-blue":            return R.color.gulf_blue;
            case "ussr-theme-resolution-blue":      return R.color.resolution_blue;
            case "ussr-theme-torea-bay":            return R.color.torea_bay;
            case "ussr-theme-parsley":              return R.color.parsley;
            case "ussr-theme-salem":                return R.color.salem;
            case "ussr-theme-forest-green":         return R.color.forest_green;
            default:                                return R.color.colorAccent;
        }
        // @formatter:on
    }
}
