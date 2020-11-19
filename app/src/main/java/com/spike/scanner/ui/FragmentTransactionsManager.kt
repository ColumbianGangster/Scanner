package com.spike.scanner.ui

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentsTransactionsManager(private val fm: FragmentManager) {

    fun replaceFragment(fragment: Fragment, tag: String, @IdRes container: Int,
                        addToBackStack: Boolean = false, enterAnims: Pair<Int, Int>? = null,
                        exitAnims: Pair<Int, Int>? = null) {
        val fragmentTransition = fm.beginTransaction()
        if (enterAnims != null && exitAnims != null) {
            fragmentTransition.setCustomAnimations(enterAnims.first, enterAnims.second, exitAnims.first, exitAnims.second)
        }

        fragmentTransition
                .replace(container, fragment, tag)

        if (addToBackStack) {
            fragmentTransition.addToBackStack(tag)
        }
        fragmentTransition.commit()
    }

    fun addFragment(fragment: Fragment, tag: String, @IdRes container: Int, addToBackStack: Boolean = false, anims: Pair<Int, Int>? = null) {
        val fragmentTransition = fm.beginTransaction()

        if (anims != null) {
            fragmentTransition.setCustomAnimations(anims.first, anims.second, anims.first, anims.second)
        }

        fragmentTransition
                .add(container, fragment, tag)

        if (addToBackStack) {
            fragmentTransition.addToBackStack(tag)
        }
        fragmentTransition.commit()
    }
}