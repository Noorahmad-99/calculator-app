package com.example.reactivexio
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import bsh.Interpreter
import com.example.reactivexio.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        calculatorApp()

    }


    private fun calculatorApp() {
        Observable.create<String> { emitter ->
            binding.input.doOnTextChanged { text, start, before, count ->
                    emitter.onNext(text.toString())
               }
            }
            .subscribeOn(Schedulers.io())

            .debounce(1.5.toLong(),TimeUnit.SECONDS)
            .subscribe (
                { onNext ->
                    binding.result.text = foo(onNext).toString()
                },
                   { onError->

                       Log.i(tag , "this is Error")
                   })
    }



    private fun foo(it: String?):Double{
        val interpreter = Interpreter()
        interpreter.eval("result =$it")
        if (it != null) {
            if (it.isEmpty())
                interpreter.eval("result = ${0.0}")
            else
                interpreter.eval("result = $it")
        }

        return interpreter.get("result").toString().toDouble()
    }

    companion object {
        const val tag = "calculator"
    }

}
