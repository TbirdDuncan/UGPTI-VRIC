package com.example.nav.ui.camera
import androidx.fragment.app.Fragment



abstract class BaseActivity : Fragment() {

    fun getRepository() = (getActivity()?.application as nav).getRepository()


}