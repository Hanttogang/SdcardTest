package team.everywhere.sdcardtest.utils

import android.os.Environment

class ExternalStorage {
    companion object {

        fun isExternalStorageReadOnly(): Boolean {
            var extStorageState = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
                return true
            }
            return false
        }

        fun isExternalStorageAvailable(): Boolean {
            var extStorageState = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == extStorageState) {
                return true
            }
            return false
        }

    }
}