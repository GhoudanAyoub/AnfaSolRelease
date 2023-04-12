package ghoudan.anfaSolution.com

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay

fun <T> Fragment.setBackStackData(key: String, data: T?, doBack: Boolean = true) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
    if (doBack)
        lifecycleScope.launchWhenResumed {
            delay(1000)
            findNavController().popBackStack()
        }

}

fun <T> Fragment.getBackStackData(key: String, result: (T?) -> (Unit)) {
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
        ?.observe(viewLifecycleOwner) {
            result(it)
        }
}
