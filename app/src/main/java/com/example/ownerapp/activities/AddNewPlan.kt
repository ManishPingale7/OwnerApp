package com.example.ownerapp.activities

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.ownerapp.R
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()


        binding.btnConPlan.setOnClickListener {
            val name=binding.planNameEdit.text.toString()
            val desc=binding.descPlanEdit.text.toString()
            val timeNumber=binding.numberDays.text.toString()
            val type=binding.typeTime.text.toString()
            val fees=binding.feesPlan.text.toString()

            if (name.isNotEmpty()&&desc.isNotEmpty()&&timeNumber.isNotEmpty()&&type.isNotEmpty()&&fees.isNotEmpty()){
                viewModel.repository.addNewPlan(Plan(name = name,desc,timeNumber,type,fees))
                viewModel.repository.sendUserToMainActivity()
            }else{
                Toast.makeText(this, "Fill the Fields", Toast.LENGTH_SHORT).show()
            }

        }


    }
    private fun init(){
        //Toolbar stuff
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.my_statusbar_color)


        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(PlanRepository(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(NewPlanViewModel::class.java)
    }
}