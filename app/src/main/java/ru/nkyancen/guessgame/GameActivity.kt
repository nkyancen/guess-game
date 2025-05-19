package ru.nkyancen.guessgame

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.nkyancen.guessgame.databinding.ActivityGameBinding
import ru.nkyancen.guessgame.utils.Game
import ru.nkyancen.guessgame.utils.GameState
import ru.nkyancen.guessgame.utils.GameState.*

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val game = Game()
    private var userInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityGameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setAttemptsToTextView()
        setUserInputToTextView()

        setButtonsOnClick()
    }

    private class AllButtons(binding: ActivityGameBinding) {
        val btnNumbers = mapOf<Int, Button>(
            0 to binding.btnZero,
            1 to binding.btnOne,
            2 to binding.btnTwo,
            3 to binding.btnThree,
            4 to binding.btnFour,
            5 to binding.btnFive,
            6 to binding.btnSix,
            7 to binding.btnSeven,
            8 to binding.btnEight,
            9 to binding.btnNine
        )
        val btnEnter = binding.btnEnter
        val btnErase = binding.btnErase
        val btnRestart = binding.btnRestart
    }

    private fun setButtonsOnClick() {
        val allButtons = AllButtons(binding)

        allButtons.btnNumbers.forEach { (key, value) ->
            value.setOnClickListener { onClickNumber(key) }
        }
        allButtons.btnErase.setOnClickListener {
            eraseNumber()
        }
        allButtons.btnEnter.setOnClickListener {
            enterNumber()
        }
        allButtons.btnRestart.setOnClickListener {
            restartGame()
        }
    }

    private fun manageButtonsClickability(isClickable: Boolean) {
        val allButtons = AllButtons(binding)

        allButtons.btnNumbers.forEach { it.value.isClickable = isClickable }
        allButtons.btnEnter.isClickable = isClickable
        allButtons.btnErase.isClickable = isClickable
    }

    private fun setAttemptsToTextView(message: String = "") {
        if (message.isNotEmpty()) {
            binding.textAttemptsCount.text = message
        } else {
            binding.textAttemptsCount.text = resources.getQuantityString(
                R.plurals.attempts_count, game.attemptsCount, game.attemptsCount
            )
        }
    }

    private fun setUserInputToTextView(message: String = "") {
        binding.textUserInput.text = if (message.isEmpty()) {
            resources.getString(R.string.input_number)
        } else {
            message
        }
    }

    private fun showToastMessage(state: GameState) {
        Toast.makeText(
            this,
            resources.getStringArray(R.array.messages)[state.index],
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun restartGame() {
        game.restartGame()
        userInput = ""
        manageButtonsClickability(true)
        setUserInputToTextView()
        setAttemptsToTextView()
    }

    private fun onClickNumber(number: Int) {
        if (userInput.length < 9) {
            userInput += number
            setUserInputToTextView(
                userInput
            )
        } else {
            showToastMessage(LONG)
        }
    }

    private fun enterNumber() {
        var gameMessage = EMPTY
        if (hasNumber()) {
            gameMessage = game.analyzeInput(userInput.toInt())
        }

        showToastMessage(gameMessage)
        setAttemptsToTextView()

        analyseGameMessage(gameMessage)
        if (gameMessage != OUT) {
            userInput = ""
        }
    }

    private fun analyseGameMessage(msg: GameState) {
        when (msg) {
            WIN -> {
                manageButtonsClickability(false)
                setAttemptsToTextView(
                    resources.getString(R.string.win)
                )
            }

            LOSE -> {
                manageButtonsClickability(false)
                setAttemptsToTextView(
                    resources.getString(R.string.lose)
                )
                setUserInputToTextView(
                    resources.getString(R.string.show_number, game.randomNumber)
                )
            }

            OUT -> {}
            else -> {
                setUserInputToTextView()
            }
        }
    }

    private fun eraseNumber() {
        if (hasNumber()) {
            userInput = userInput.dropLast(1)
        }
        setUserInputToTextView(userInput)
    }

    private fun hasNumber(): Boolean {
        return if (userInput.isEmpty()) {
            showToastMessage(EMPTY)
            false
        } else {
            true
        }
    }
}