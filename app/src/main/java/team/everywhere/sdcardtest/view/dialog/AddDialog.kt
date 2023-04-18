package team.everywhere.sdcardtest.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import team.everywhere.sdcardtest.databinding.DialogAddBinding
import java.io.File
import java.io.FileWriter
import java.io.IOException

class AddDialog(var path: String, var onAddFile: OnAddFile) : DialogFragment() {
    interface OnAddFile {
        fun addFile(fileName:String)
        fun addDirectory(dirName:String)
    }

    lateinit var binding: DialogAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddBinding.inflate(inflater, container, false)

        setBindings()

        return binding.root
    }

    private fun setBindings() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }

        binding.tvBuild.setOnClickListener {
            var fileName = binding.etFileName.text.toString()
            if (fileName.isBlank()) {

            } else {
                try {
                    if(fileName.contains(".")){
//                    var writer = FileWriter(file)
                        onAddFile.addFile(fileName)
                    } else {
                        onAddFile.addDirectory(fileName)
                    }
//                    var ext = fileName.split(".")[1]
//                    var name = fileName.split(".")[0]

                    dismiss()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

}