package com.submisi2.githubuser.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.submisi2.githubuser.FollowerFragment
import com.submisi2.githubuser.FollowingFragment
import com.submisi2.githubuser.R

@Suppress("DEPRECATION")
class SectionsAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val PUT_EXTRA = "extra_parcelable"
        private lateinit var username: String

        @StringRes
        private val titleTab = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
    override fun getCount(): Int {
        return 2
    }
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        fun newInstance(){
            val mBundle = Bundle()
            mBundle.putString(PUT_EXTRA, getUsername())
            fragment?.arguments = mBundle
        }
        when (position) {
            0 -> {
                fragment = FollowerFragment()
                newInstance()

            }
            1 -> {
                fragment = FollowingFragment()
                newInstance()
            }
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(titleTab[position])
    }

    fun setUsername(dataUsername: String) {
        username = dataUsername
    }

    private fun getUsername() : String? = username

}