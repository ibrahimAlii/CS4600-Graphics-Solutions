package cs4600.ibrahim.project1

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.google.android.material.button.MaterialButton
import java.io.FileNotFoundException
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    val RESULT_LOAD_IMG = 150
    var editorLayout: PhotoEditorLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addImage = findViewById<MaterialButton>(R.id.addPhoto)
        val control = findViewById<MaterialButton>(R.id.hideControls)
        editorLayout = findViewById(R.id.editorLayout)
        val spinner = findViewById<AppCompatSpinner>(R.id.type)


        addImage.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
        }

        var isControlsHidden = false
        control.setOnClickListener {
            if (isControlsHidden) {
                editorLayout!!.showControls()
                isControlsHidden = false
                control.text = "Hide Controls"
            } else {
                editorLayout!!.hideControls()
                isControlsHidden = true
                control.text = "Show Controls"
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            try {
                val imageUri: Uri = data!!.data!!
                val imageStream: InputStream? = contentResolver.openInputStream(imageUri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                editorLayout!!.addImage(selectedImage)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }
}