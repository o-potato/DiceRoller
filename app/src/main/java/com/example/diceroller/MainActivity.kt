package com.example.diceroller

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initially show picture dice_4
        val imageView: ImageView = findViewById(R.id.imageView)
        imageView.setImageResource(R.drawable.dice_4)

        //when cheatSwitch is on, long click on ROLL will always get target number
        //when is off, long click doesn't work
        val cheatSwitch: Switch = findViewById(R.id.cheatSwitch)
        var wantToCheat = false
        cheatSwitch.setOnCheckedChangeListener{ _, isChecked ->
            wantToCheat = isChecked
        }

        val auto: Button = findViewById(R.id.autoButton)
        val manual: Button = findViewById(R.id.manualButton)
        val getIt: Button = findViewById(R.id.clickButton)
        val autoTargetTextView: TextView = findViewById(R.id.autoTarget)
        val input: EditText = findViewById(R.id.input)
        val minus: ImageButton = findViewById(R.id.minusImageButton)
        val plus: ImageButton = findViewById(R.id.plusImageButton)

        val dark = resources.getColor(R.color.dark_background)
        val light = resources.getColor(R.color.white_background)

        //auto button logic
        //auto mode by default
        //when in manual mode, click auto button to switch to auto mode
        auto.setOnClickListener(object: ProxyClickListener() {
            override fun doOnClick(v: View) {
                auto.setTextColor(dark)
                auto.setBackgroundColor(light)
                manual.setTextColor(light)
                manual.setBackgroundColor(dark)
                getIt.visibility = View.VISIBLE
                autoTargetTextView.visibility = View.VISIBLE
                input.visibility = View.INVISIBLE
                minus.visibility = View.INVISIBLE
                plus.visibility = View.INVISIBLE
            }
        })
        /*
        auto.setOnClickListener {
            auto.setTextColor(dark)
            auto.setBackgroundColor(light)
            manual.setTextColor(light)
            manual.setBackgroundColor(dark)
            getIt.visibility = View.VISIBLE
            autoTargetTextView.visibility = View.VISIBLE
            input.visibility = View.INVISIBLE
            minus.visibility = View.INVISIBLE
            plus.visibility = View.INVISIBLE
        }*/

        //manual button logic
        //when in auto mode, click manual button to switch to manual mode
        manual.setOnClickListener(object : ProxyClickListener(){
            override fun doOnClick(v: View) {
                manual.setTextColor(dark)
                manual.setBackgroundColor(light)
                auto.setTextColor(light)
                auto.setBackgroundColor(dark)
                getIt.visibility = View.INVISIBLE
                autoTargetTextView.visibility = View.INVISIBLE
                input.visibility = View.VISIBLE
                minus.visibility = View.VISIBLE
                plus.visibility = View.VISIBLE
            }
        })
        /*
        manual.setOnClickListener {
            manual.setTextColor(dark)
            manual.setBackgroundColor(light)
            auto.setTextColor(light)
            auto.setBackgroundColor(dark)
            getIt.visibility = View.INVISIBLE
            autoTargetTextView.visibility = View.INVISIBLE
            input.visibility = View.VISIBLE
            minus.visibility = View.VISIBLE
            plus.visibility = View.VISIBLE
        }*/

        var target = 0

        //GET IT button logic
        //click GET IT button to auto choose a target
        //and show the target
        getIt.setOnClickListener(object : ProxyClickListener(){
            override fun doOnClick(v: View) {
                target = (1..6).random()
                autoTargetTextView.text = target.toString()
            }
        })
        /*
        getIt.setOnClickListener{
            target = (1..6).random()
            autoTargetTextView.text = target.toString()
        }*/

        //plus button logic
        plus.setOnClickListener(object : ProxyClickListener(){
            override fun doOnClick(v: View) {
                target = input.text.toString().toInt()
                if(target < 6){
                    target++
                    input.setText(target.toString())
                }
                else{
                    Toast.makeText(this@MainActivity, "Already the max target!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        /*
        plus.setOnClickListener {
            target = input.text.toString().toInt()
            if(target < 6){
                target++
                input.setText(target.toString())
            }
            else{
                Toast.makeText(this, "Already the max target!", Toast.LENGTH_SHORT)
                    .show()
            }
        }*/

        //minus button logic
        minus.setOnClickListener(object : ProxyClickListener(){
            override fun doOnClick(v: View) {
                target = input.text.toString().toInt()
                if(target > 1){
                    target--
                    input.setText(target.toString())
                }
                else{
                    Toast.makeText(this@MainActivity, "Already the min target!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        /*
        minus.setOnClickListener {
            target = input.text.toString().toInt()
            if(target > 1){
                target--
                input.setText(target.toString())
            }
            else{
                Toast.makeText(this, "Already the min target!", Toast.LENGTH_SHORT)
                    .show()
            }
        }*/

        //EditText logic
        //check: 1 <= input <= 6
        //and input can not be empty
        //TODO: when delete the existing number, the app exits abnormally
        input.filters = arrayOf(InputFilterMinMax(1, 6))


        //ROLL button logic
        val rollButton: Button = findViewById(R.id.rollButton)
        //for long click:
        //if wantToCheat is on, check if there is a target
        //show noTargetDialog unless a target is set
        //otherwise, roll normally
        rollButton.setOnLongClickListener(object : ProxyLongClickListener(){
            override fun doOnLongClick(v: View) {
                if(wantToCheat){
                    if(target == 0){
                        showNoTargetDialog()
                    }
                    else{
                        setImage(target, imageView)
                        showSuccessDialog()
                    }
                }
                else{
                    if(rollDice() == target){
                        showSuccessDialog()
                    }
                }
                //rollDice(wantToCheat)
                //true
            }
        })
        /*
        rollButton.setOnLongClickListener {
            if(wantToCheat){
                if(target == 0){
                    showNoTargetDialog()
                }
                else{
                    setImage(target, imageView)
                    showSuccessDialog()
                }
            }
            else{
                if(rollDice() == target){
                    showSuccessDialog()
                }
            }
            //rollDice(wantToCheat)
            true
        }*/

        //for short click:
        //roll normally
        rollButton.setOnClickListener(object : ProxyClickListener(){
            override fun doOnClick(v: View) {
                if(rollDice() == target){
                    showSuccessDialog()
                }
            }
        })
        /*
        rollButton.setOnClickListener {
            if(rollDice() == target){
                showSuccessDialog()
            }
        }*/
    }

    private fun showNoTargetDialog(){
        val noTargetBuilder = AlertDialog.Builder(this)
        noTargetBuilder.setTitle(R.string.no_target_title)
        noTargetBuilder.setMessage(R.string.no_target)
        noTargetBuilder.setPositiveButton(R.string.set,
            dialogClick()?.let { DialogInterface.OnClickListener(it) })
        noTargetBuilder.setNegativeButton(R.string.no_need,
            dialogClick()?.let { DialogInterface.OnClickListener(it) })
        noTargetBuilder.show()
    }

    private fun showSuccessDialog(){
        val successBuilder = AlertDialog.Builder(this)
        successBuilder.setTitle(R.string.cong)
        successBuilder.setMessage(R.string.succ)
        successBuilder.show()
    }

    private fun dialogClick(): ((DialogInterface, Int) -> Unit)? {
        return null
    }

    private fun rollDice(): Int{
        val myDice = Dice(6)
        //var diceRoll = target;

        val diceRoll = myDice.roll()
        val diceImage: ImageView = findViewById(R.id.imageView)
        setImage(diceRoll, diceImage)
        return diceRoll
    }

    //show correct picture
    private fun setImage(diceRoll: Int, diceImage: ImageView) {
        val diceImageResource = when(diceRoll){
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        diceImage.setImageResource(diceImageResource)
        diceImage.contentDescription = diceRoll.toString()
    }
}

class Dice(private val numberSides: Int){
    fun roll(): Int{
        return (1..numberSides).random()
    }
}

class InputFilterMinMax(private val min: Int, private val max: Int) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        var newVal = dest.toString().substring(0, dstart) +
                dest.toString().substring(dend, dest.toString().length)
        newVal = newVal.substring(0, dstart) + source.toString() +
                newVal.substring(dstart, newVal.length)
        val input = Integer.parseInt(newVal)
        if(isInRange(min, max, input)){
            return newVal
        }
        return ""
    }

    private fun isInRange(a: Int, b: Int, c:Int): Boolean{
        return if (b > a) c in a..b else c in b..a
    }

}