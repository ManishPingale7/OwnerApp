package com.example.ownerapp.activities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.ownerapp.R
import com.example.ownerapp.Utils.ProgressBtn
import com.example.ownerapp.data.Plan
import com.example.ownerapp.databinding.ActivityAddNewPlanBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.PlanRepository
import com.example.ownerapp.mvvm.viewmodles.NewPlanViewModel

class AddNewPlan : AppCompatActivity() {
    private lateinit var viewModel: NewPlanViewModel
    private lateinit var component: DaggerFactoryComponent
    lateinit var binding: ActivityAddNewPlanBinding
    var onceClicked = false
    var isPersonalTraning = false
    var arraylistType = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        val durationType = resources.getStringArray(R.array.durationType)
        arraylistType = ArrayList<String>(listOf(*resources.getStringArray(R.array.durationType)))
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdownitem, durationType)
        binding.typeTime.setAdapter(arrayAdapter)

        Log.d(TAG, "onCreate: ${arraylistType.size}")

        binding.switchPt.setOnCheckedChangeListener { _, isChecked ->
            isPersonalTraning = isChecked
        }


        val view = findViewById<View>(R.id.btn_con_plan2)

        view.setOnClickListener {
            val progressBtn = ProgressBtn(this, view)
            val name = binding.planNameEdit.text.toString()
            val desc = binding.descPlanEdit.text.toString()
            val timeNumber = binding.numberDays.text.toString()
            var type = binding.typeTime.text.toString()
            val fees = binding.feesPlan.text.toString()

            if (name.isNotEmpty() && desc.isNotEmpty() && timeNumber.isNotEmpty() && type.isNotEmpty() && fees.isNotEmpty()) {
                if (arraylistType.contains(type)) {
                    if (timeNumber.toInt() > 0) {
                        type += "s"
                    }

                    if (!onceClicked) {
                        progressBtn.buttonActivated()
                        viewModel.repository.addNewPlan(
                            Plan(
                                name,
                                desc,
                                "$timeNumber $type",
                                fees,
                                isPersonalTraning
                            )
                        )
                        onceClicked = true
                        Handler(Looper.getMainLooper()).postDelayed({
                            progressBtn.buttonfinished()
                            viewModel.repository.sendUserToMainActivity()
                            finish()
                        }, 3000)
                    }
                } else {
                    Toast.makeText(this, "Wrong Duration Type", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill the Fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun init(){
        //Toolbar stuff
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorprimarymain)

        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(PlanRepository(this)))
            .build() as DaggerFactoryComponent

        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(NewPlanViewModel::class.java)

    }
}