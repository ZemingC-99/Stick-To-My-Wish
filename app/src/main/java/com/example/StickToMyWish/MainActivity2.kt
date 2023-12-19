package com.example.StickToMyWish

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.example.StickToMyWish.MyApplication.Companion.projectIndex
import com.example.StickToMyWish.entity.Project
import com.example.StickToMyWish.entity.Record
import com.example.StickToMyWish.util.DialogUtil
import com.example.StickToMyWish.util.ImageUtil
import com.example.StickToMyWish.util.QuoteFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID

class MainActivity2 : BaseActivity() {
    private var title: TextView? = null
    private var arrow_left: ImageView? = null
    private var arrow_right: ImageView? = null
    private var project: Project? = null
    private val monDisplayList: MutableList<String> = ArrayList()
    private val monList: MutableList<String> = ArrayList()
    private val dayList: MutableList<String> = ArrayList()
    private var currentmon: String? = null
    private var currentday: String? = null
    private var detail_data_layout1: LinearLayout? = null
    private var detail_data_layout2: LinearLayout? = null
    private var detail_data_layout3: LinearLayout? = null
    private var detail_data_layout4: LinearLayout? = null
    private var detail_data_layout5: LinearLayout? = null
    private var detail_data_layout6: LinearLayout? = null
    private var detail_data_layout7: LinearLayout? = null
    private var setting: TextView? = null
    private var share: TextView? = null
    private val layoutList: MutableList<LinearLayout?> = ArrayList()
    private var shapeableImageView: ArcView? = null
    private var detail_content: TextView? = null
    private var between: TextView? = null
    private val totalDate = 0
    private var records: List<Record>? = null
    private var selectDay: Int = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        title = findViewById(R.id.title)
        arrow_left = findViewById(R.id.image_arrow_left)
        arrow_right = findViewById(R.id.image_arrow_right)
        for (i in -3..3) {
            if (i == 0) {
                currentday = LocalDateTime.now().plusDays(i.toLong()).month.name.substring(0, 3)
                currentmon = LocalDateTime.now().plusDays(i.toLong())
                    .format(DateTimeFormatter.ofPattern("MM"))
            }
            monDisplayList.add(LocalDateTime.now().plusDays(i.toLong()).month.name.substring(0, 3))
            monList.add(
                LocalDateTime.now().plusDays(i.toLong()).format(DateTimeFormatter.ofPattern("MM"))
            )
            dayList.add(
                LocalDateTime.now().plusDays(i.toLong()).format(DateTimeFormatter.ofPattern("dd"))
            )
        }
        initView()
        init(projectIndex)
    }

    private fun init(index: Int) {
        project = MyApplication.projectList[index]
        title!!.text = project!!.title
        detail_content!!.text = project!!.content
        between!!.text = " / " + getDaysBetween(project!!.start, project!!.end)
        arrow_right!!.visibility = View.VISIBLE
        arrow_left!!.visibility = View.VISIBLE
        if (projectIndex == MyApplication.projectList.size - 1) {
            arrow_right!!.visibility = View.GONE
        }
        if (projectIndex == 0) {
            arrow_left!!.visibility = View.GONE
        }

        getRecordData(project!!)

        arrow_right!!.setOnClickListener {
            init(++projectIndex)
        }

        arrow_left!!.setOnClickListener {
            init(--projectIndex)
        }

    }

    private fun initView() {
        between = findViewById(R.id.between)
        detail_content = findViewById(R.id.detail_content)
        detail_data_layout1 = findViewById(R.id.detail_data_layout1)
        detail_data_layout2 = findViewById(R.id.detail_data_layout2)
        detail_data_layout3 = findViewById(R.id.detail_data_layout3)
        detail_data_layout4 = findViewById(R.id.detail_data_layout4)
        detail_data_layout5 = findViewById(R.id.detail_data_layout5)
        detail_data_layout6 = findViewById(R.id.detail_data_layout6)
        detail_data_layout7 = findViewById(R.id.detail_data_layout7)
        shapeableImageView = findViewById(R.id.detail_content_layout)
        setting = findViewById(R.id.setting)
        share = findViewById(R.id.share)

        share!!.setOnClickListener {
            val items = arrayListOf(
                BasicGridItem(R.drawable.facebook, "facebook"),
                BasicGridItem(R.drawable.twitter, "twitter"),
            )
            DialogUtil().showBottomGridDialog(this, items) { dialog, index, item ->
                when (item.title) {
                    "facebook" -> shareOnFacebook()
                    "twitter" -> shareOnTwitter()
                    "instagram" -> shareOnInstagram()
                }
            }
        }

        setting!!.setOnClickListener {
            var intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        layoutList.add(detail_data_layout1)
        layoutList.add(detail_data_layout2)
        layoutList.add(detail_data_layout3)
        layoutList.add(detail_data_layout4)
        layoutList.add(detail_data_layout5)
        layoutList.add(detail_data_layout6)
        layoutList.add(detail_data_layout7)
        for (i in layoutList.indices) {
            val day = layoutList[i]!!.findViewWithTag<TextView>("day")
            val month = layoutList[i]!!.findViewWithTag<TextView>("month")
            day.text = dayList[i]
            month.text = monDisplayList[i]
        }
        shapeableImageView!!.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val record = Record(
                    UUID.randomUUID().toString(),
                    project!!.id,
                    LocalDateTime.now().plusDays(0).format(DateTimeFormatter.ofPattern("MM-dd"))
                )
                MyDataBase.instance!!.recordDao().insert(record);
                runOnUiThread {
                    getRecordData(project!!);
                }
                fetchAndDisplayQuote()
            }
        }
    }

    private fun getRecordData(project: Project) {
        lifecycleScope.launch(Dispatchers.IO) {
            records = MyDataBase.instance!!.recordDao().getRecordList(project.id);
            runOnUiThread {
                showRecodeInfo(monList[selectDay], dayList[selectDay])
                for (i in layoutList.indices) {
                    val image = layoutList[i]!!.findViewWithTag<ImageView>("image")
                    image.setImageDrawable(resources.getDrawable(R.drawable.fail))
                    if (showDateInfo(monList[i], dayList[i])) {
                        image.setImageDrawable(resources.getDrawable(R.drawable.success))
                    }
                    layoutList[i]!!.setOnClickListener {
                        selectDay = i
                        showRecodeInfo(monList[selectDay], dayList[selectDay])
                    }
                }
                between!!.text = resources.getString(
                    R.string.between,
                    "${records!!.size}",
                    "${getDaysBetween(project!!.start, project!!.end)}"
                )
            }
        }

    }

    private fun showDateInfo(mon: String, day: String): Boolean {
        return records!!.stream().anyMatch {
            it.date.equals("${mon}-${day}")
        }
    }

    private fun showRecodeInfo(mon: String, day: String) {
        shapeableImageView!!.mIncludedAngle =
            (records!!.size * 360 / getDaysBetween(project!!.start, project!!.end)).toFloat()
        if (showDateInfo(mon, day)) {
            shapeableImageView!!.color = Color.GREEN
        } else {
            shapeableImageView!!.color = Color.GRAY
        }
        shapeableImageView!!.invalidate()
    }

    private fun getDaysBetween(start: String, end: String): Long {
        val a = start.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val b = end.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val startDate = LocalDate.of(a[0].toInt(), a[1].toInt(), a[2].toInt())
        val endDate = LocalDate.of(b[0].toInt(), b[1].toInt(), b[2].toInt())
        return ChronoUnit.DAYS.between(startDate, endDate)
    }

    override fun onResume() {
        super.onResume()

        val root = findViewById<View>(R.id.view8)
        val layout1 = findViewById<View>(R.id.layout1)
        val title = findViewById<TextView>(R.id.title)
        val leftArrow = findViewById<ImageView>(R.id.image_arrow_left)
        val rightArrow = findViewById<ImageView>(R.id.image_arrow_right)

        val backgroundColor: Int = ContextCompat.getColor(
            this, if (MyApplication.darkMode) R.color.black else R.color.white
        )
        val displayColor: Int = ContextCompat.getColor(
            this, if (MyApplication.darkMode) R.color.white else R.color.black
        )

        val leftArrowImg = ContextCompat.getDrawable(this, R.drawable.arrow_left)
        val rightArrowImg = ContextCompat.getDrawable(this, R.drawable.arrow_right)

        val drawable1: Drawable? = ImageUtil().tintDrawable(leftArrowImg, displayColor)
        leftArrow.setImageDrawable(drawable1)
        val drawable2: Drawable? = ImageUtil().tintDrawable(rightArrowImg, displayColor)
        rightArrow.setImageDrawable(drawable2)

        root.setBackgroundColor(backgroundColor)
        layout1.setBackgroundColor(backgroundColor)
        title.setTextColor(displayColor)
        for (i in layoutList.indices) {
            val day = layoutList[i]!!.findViewWithTag<TextView>("day")
            val month = layoutList[i]!!.findViewWithTag<TextView>("month")
            day.setTextColor(displayColor)
            month.setTextColor(displayColor)
        }
        between!!.setTextColor(displayColor)
        share!!.setTextColor(displayColor)
        setting!!.setTextColor(displayColor)
    }

    private fun fetchAndDisplayQuote() {
        val quoteFetcher = QuoteFetcher()
        quoteFetcher.fetchQuote { result ->
            runOnUiThread {
                try {
                    // Parse the JSON response
                    val jsonArray = JSONArray(result)
                    val jsonObject = jsonArray.getJSONObject(0)
                    val quote = jsonObject.getString("q")
                    val author = jsonObject.getString("a")

                    // Display the parsed quote and author in a dialog
                    AlertDialog.Builder(this)
                        .setMessage("\"$quote\"\n\n- $author")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                } catch (e: JSONException) {
                    AlertDialog.Builder(this)
                        .setMessage("Error parsing the quote")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
    }

    private fun shareOnFacebook() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Your text here")
        // If you want to share an image or other media, you set its URI in the intent
        // shareIntent.putExtra(Intent.EXTRA_STREAM, mediaUri)
        // shareIntent.type = "image/jpeg" // Use appropriate MIME type based on content

        // Attempt to direct the intent specifically to the Facebook app
        shareIntent.setPackage("com.facebook.katana")

        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            // If the Facebook app is not installed, show a message or handle otherwise
            Toast.makeText(this, "Facebook app is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareOnTwitter() {
        val tweetIntent = Intent(Intent.ACTION_SEND)
        tweetIntent.type = "text/plain"
        tweetIntent.putExtra(Intent.EXTRA_TEXT, "Your text here")

        // Attempt to direct the intent specifically to the Twitter app
        tweetIntent.setPackage("com.twitter.android")

        try {
            startActivity(tweetIntent)
        } catch (ex: ActivityNotFoundException) {
            // If the Twitter app is not installed, handle the exception, e.g., show a toast
            Toast.makeText(this, "Twitter app is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareOnInstagram() {
        val shareIntent = Intent(Intent.ACTION_SEND)

        // Set MIME type and URI for the image to be shared
        shareIntent.type = "image/*"

        // Attempt to direct the intent specifically to the Instagram app
        shareIntent.setPackage("com.instagram.android")

        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            // If the Instagram app is not installed, handle the exception, e.g., show a toast
            Toast.makeText(this, "Instagram app is not installed", Toast.LENGTH_SHORT).show()
        }
    }

}