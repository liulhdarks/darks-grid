/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package darks.grid.utils;

import darks.grid.commons.PlatformType;

public final class OSUtils {

    private static String OS = System.getProperty("os.name").toLowerCase();

    private static PlatformType platform;
    
    static
    {
        initPlatformType();
    }
    
    private OSUtils() {
        
    }

    public static boolean isLinux() {
        return OS.indexOf("linux") >= 0;
    }

    public static boolean isMacOS() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") < 0;
    }

    public static boolean isMacOSX() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
    }

    public static boolean isWindows() {
        return OS.indexOf("windows") >= 0;
    }

    public static boolean isOS2() {
        return OS.indexOf("os/2") >= 0;
    }

    public static boolean isSolaris() {
        return OS.indexOf("solaris") >= 0;
    }

    public static boolean isSunOS() {
        return OS.indexOf("sunos") >= 0;
    }

    public static boolean isMPEiX() {
        return OS.indexOf("mpe/ix") >= 0;
    }

    public static boolean isHPUX() {
        return OS.indexOf("hp-ux") >= 0;
    }

    public static boolean isAix() {
        return OS.indexOf("aix") >= 0;
    }

    public static boolean isOS390() {
        return OS.indexOf("os/390") >= 0;
    }

    public static boolean isFreeBSD() {
        return OS.indexOf("freebsd") >= 0;
    }

    public static boolean isIrix() {
        return OS.indexOf("irix") >= 0;
    }

    public static boolean isDigitalUnix() {
        return OS.indexOf("digital") >= 0 && OS.indexOf("unix") > 0;
    }

    public static boolean isNetWare() {
        return OS.indexOf("netware") >= 0;
    }

    public static boolean isOSF1() {
        return OS.indexOf("osf1") >= 0;
    }

    public static boolean isOpenVMS() {
        return OS.indexOf("openvms") >= 0;
    }

    public static PlatformType getPlatform() {
        return platform;
    }

    private static PlatformType initPlatformType() {
        if (isAix()) {
            platform = PlatformType.AIX;
        } else if (isDigitalUnix()) {
            platform = PlatformType.Digital_Unix;
        } else if (isFreeBSD()) {
            platform = PlatformType.FreeBSD;
        } else if (isHPUX()) {
            platform = PlatformType.HP_UX;
        } else if (isIrix()) {
            platform = PlatformType.Irix;
        } else if (isLinux()) {
            platform = PlatformType.Linux;
        } else if (isMacOS()) {
            platform = PlatformType.Mac_OS;
        } else if (isMacOSX()) {
            platform = PlatformType.Mac_OS_X;
        } else if (isMPEiX()) {
            platform = PlatformType.MPEiX;
        } else if (isNetWare()) {
            platform = PlatformType.NetWare_411;
        } else if (isOpenVMS()) {
            platform = PlatformType.OpenVMS;
        } else if (isOS2()) {
            platform = PlatformType.OS2;
        } else if (isOS390()) {
            platform = PlatformType.OS390;
        } else if (isOSF1()) {
            platform = PlatformType.OSF1;
        } else if (isSolaris()) {
            platform = PlatformType.Solaris;
        } else if (isSunOS()) {
            platform = PlatformType.SunOS;
        } else if (isWindows()) {
            platform = PlatformType.Windows;
        } else {
            platform = PlatformType.Others;
        }
        return platform;
    }

}
