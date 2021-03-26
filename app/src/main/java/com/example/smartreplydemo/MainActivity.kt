package com.github.rotolonico.firebasesmartreply
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.smartreplydemo.R
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult
//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var conversation = ArrayList<FirebaseTextMessage>()
    val clearButton = findViewById<Button>(R.id.clearButton)
    val hint0Button = findViewById<Button>(R.id.hint0Button)
    val hint1Button = findViewById<Button>(R.id.hint1Button)
    val hint2Button = findViewById<Button>(R.id.hint2Button)
    val errorText= findViewById(R.id.errorText) as TextView
    val nameText= findViewById(R.id.nameText) as TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val messageText = findViewById(R.id.messageText) as TextView
        val sendButton = findViewById<Button>(R.id.sendButton)
        sendButton.setOnClickListener {
            addMessage(messageText.text.toString())
        }
        val hintsButton= findViewById<Button>(R.id.hintsButton)
        hintsButton.setOnClickListener {
            getHints()
        }

        clearButton.setOnClickListener {
            conversation = ArrayList()

            hint0Button.visibility = View.GONE

            hint1Button.visibility = View.GONE

            hint2Button.visibility = View.GONE

            errorText.text = ""
        }

        hint0Button.setOnClickListener {
            addMessage(hint0Button.text.toString())
        }

        hint1Button.setOnClickListener {
            addMessage(hint1Button.text.toString())
        }

        hint2Button.setOnClickListener {
            addMessage(hint2Button.text.toString())
        }
    }

    private fun addMessage(text : String){
        conversation.add(
                FirebaseTextMessage.createForRemoteUser(
                        text, System.currentTimeMillis(), nameText.text.toString()))
    }

    private fun getHints(){
        val smartReply = FirebaseNaturalLanguage.getInstance().smartReply
        smartReply.suggestReplies(conversation)
                .addOnSuccessListener { result ->
                    if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                        errorText.text = "Language not supported"
                    } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                        hint0Button.text = result.suggestions[0].text
                        hint1Button.text = result.suggestions[1].text
                        hint2Button.text = result.suggestions[2].text

                        hint0Button.visibility = View.VISIBLE
                        hint1Button.visibility = View.VISIBLE
                        hint2Button.visibility = View.VISIBLE
                        errorText.text = ""
                    }
                }
                .addOnFailureListener {
                    errorText.text = it.toString()
                }
    }
}