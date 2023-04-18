package team.everywhere.sdcardtest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import team.everywhere.sdcardtest.adapter.TestAdapter
import team.everywhere.sdcardtest.databinding.ActivityMainBinding
import team.everywhere.sdcardtest.utils.ExternalStorage
import team.everywhere.sdcardtest.view.dialog.AddDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var fabsVis = View.GONE
    var currentPath = MutableLiveData<File>()
    lateinit var testAdapter: TestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (checkExtStorage()) {
            currentPath.value = Environment.getExternalStorageDirectory()

            currentPath.observe(this, Observer {
                binding.tvTitle.text = it.name

                binding.fabAdd.isEnabled = !it.toString().contains(".")
            })

            var filesInFolder: ArrayList<String> = getFiles(currentPath.value.toString())
            testAdapter = TestAdapter(
                this,
                filesInFolder,
                object : TestAdapter.OnItemClicked {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun itemClicked(p: Int) {
                        Log.d(TAG, "itemClicked: ${filesInFolder[p]}")
                        currentPath.value =
                            File(currentPath.value.toString() + "/" + filesInFolder[p])
                        filesInFolder = getFiles(currentPath.value.toString())
//                        Log.d(TAG, "itemClicked: $filesInFolder")
                        testAdapter.itemList = filesInFolder
                        testAdapter.notifyDataSetChanged()
                    }
                }
            )
            binding.mList.adapter = testAdapter

            binding.ivBack.setOnClickListener {
                if (currentPath.value != Environment.getExternalStorageDirectory()) {
                    currentPath.value = File(File(currentPath.value.toString()).parent?.toString())
                    filesInFolder = getFiles(currentPath.value.toString())
                    testAdapter.itemList = filesInFolder
                    testAdapter.notifyDataSetChanged()
                }
            }

            binding.fab.setOnClickListener {
                fabsVis = if (fabsVis == View.GONE) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                binding.fabAdd.visibility = fabsVis
                binding.fabDelete.visibility = fabsVis
            }

            binding.fabAdd.setOnClickListener {
                var dialog = AddDialog(currentPath.value.toString(), object : AddDialog.OnAddFile {
                    override fun addFile(fileName: String) {
                        Log.d(TAG, "addFile: called")
                        try {
                            var file = File(currentPath.value.toString(), fileName)
//                    var ext = fileName.split(".")[1]
//                    var name = fileName.split(".")[0]
                            Log.d(TAG, "addFile: ${file.absolutePath}")
                            if (file.exists()) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "이미 동일한 파일이 존재합니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                file.createNewFile()
                                var fos = FileOutputStream(file)
                                fos.flush()
                                fos.close()
                            }
//                    var writer = FileWriter(file)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } finally {
                            filesInFolder = getFiles(currentPath.value.toString())
                            testAdapter.itemList = filesInFolder
                            testAdapter.notifyDataSetChanged()
                        }

                    }

                    override fun addDirectory(dirName: String) {
                        Log.d(TAG, "addDirectory: called")
                        try {
                            var file = File(currentPath.value.toString(), dirName)

                            if (!file.exists()) {
                                file.mkdirs()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "이미 동일한 폴더가 존재합니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
//                    var writer = FileWriter(file)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } finally {
                            filesInFolder = getFiles(currentPath.value.toString())
                            testAdapter.itemList = filesInFolder
                            testAdapter.notifyDataSetChanged()
                        }
                    }
                }).show(supportFragmentManager, "AddDialog")
            }

            binding.fabDelete.setOnClickListener {
                Snackbar.make(it, "Not available", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun checkExtStorage(): Boolean =
        ExternalStorage.isExternalStorageAvailable() && !ExternalStorage.isExternalStorageReadOnly()

    private fun getFiles(directaryPath: String): ArrayList<String> {
        var myFiles = ArrayList<String>()
        var f = File(directaryPath)
        f.mkdirs()
        val files = f.listFiles()
        if (files != null) {
            if (files.isEmpty()) return ArrayList() else {
                for (i in files.indices) myFiles.add(files[i].name)
            }
        }
        return myFiles
    }
}