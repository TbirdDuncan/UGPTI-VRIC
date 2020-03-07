package com.example.nav.ui.help


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.nav.R


class HelpFragment : Fragment() {

    private lateinit var helpViewModel: HelpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        helpViewModel =
            ViewModelProviders.of(this).get(HelpViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_help, container, false)
        val textView: TextView = root.findViewById(R.id.text_help)
        helpViewModel.text.observe(this, Observer {
            textView.text = it
        })
        val simpleViewAnimator =
            root.findViewById(R.id.viewPager) as ViewAnimator //get the reference of ViewAnimator

        val hello = "Whats up"
        val btnNext: Button = root.findViewById(R.id.nxt_button) as Button //get the reference of Button

// set Click event on next button
        // set Click event on next button
        btnNext.setOnClickListener(View.OnClickListener {
            // TODO Auto-generated method stub
            // show the next view of ViewAnimator
            simpleViewAnimator.showNext()
        })

        val btnPrevious =
            root.findViewById(R.id.prv_button) as Button // get the reference of Button

// set Click event on next button
        // set Click event on next button
        btnPrevious.setOnClickListener {
            // TODO Auto-generated method stub
            // show the next view of ViewFlipper
            simpleViewAnimator.showPrevious()
        }
        return root
    }
}