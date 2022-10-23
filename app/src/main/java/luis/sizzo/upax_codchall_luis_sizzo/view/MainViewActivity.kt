package luis.sizzo.upax_codchall_luis_sizzo.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint
import luis.sizzo.upax_codchall_luis_sizzo.R
import luis.sizzo.upax_codchall_luis_sizzo.common.Dialogs
import luis.sizzo.upax_codchall_luis_sizzo.common.Utilities
import luis.sizzo.upax_codchall_luis_sizzo.databinding.ActivityMainBinding
import luis.sizzo.upax_codchall_luis_sizzo.model.remote.MyLatLngService
import luis.sizzo.upax_codchall_luis_sizzo.view.fragments.CamaraFragment
import luis.sizzo.upax_codchall_luis_sizzo.view.fragments.CloudFragment
import luis.sizzo.upax_codchall_luis_sizzo.view.fragments.MovieFragment
import luis.sizzo.upax_codchall_luis_sizzo.view.fragments.PerfilFragment
import luis.sizzo.upax_codchall_luis_sizzo.view_model.MainActivityViewModel


@AndroidEntryPoint
class MainViewActivity : AppCompatActivity() {

    private var howShowIt = true
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }
    private val dialog =  Dialogs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initOberserver()
    }

    private fun initViews() {

        val perfilFragment= PerfilFragment()
        val movieFragment= MovieFragment()
        val cloudFragment= CloudFragment()
        val camaraFragment= CamaraFragment()

        setCurrentFragment(perfilFragment)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile_option -> setCurrentFragment(perfilFragment)
                R.id.movies_option -> setCurrentFragment(movieFragment)
                R.id.storage_option -> setCurrentFragment(cloudFragment)
                R.id.camera_option -> setCurrentFragment(camaraFragment)
            }
            true
        }

        if (!Utilities.isStoragePermissionGranted(this)) {
            Dialogs().bottomDialogCerrarSesion(this, this)
        }



        startService(Intent(this@MainViewActivity, MyLatLngService::class.java))

//        binding.btnSearch.click {
//            if(binding.searchUser.text.toString().isNotEmpty()){
//                viewModel.getSearchUser(binding.searchUser.text.toString())
//            }else{
//                binding.lyViewList.setVisibility(View.GONE);
//                binding.shimmerViewContainer.stopShimmerAnimation();
//                binding.shimmerViewContainer.setVisibility(View.GONE);
//            }
//        }
//        binding.swipeRefresh.setOnRefreshListener {
//            if(binding.searchUser.text.toString().isNotEmpty()){
//                viewModel.getSearchUser(binding.searchUser.text.toString())
//            }else{
//                binding.swipeRefresh.isRefreshing = false
//                binding.lyViewList.setVisibility(View.GONE)
//                binding.shimmerViewContainer.stopShimmerAnimation();
//                binding.shimmerViewContainer.setVisibility(View.GONE);
//            }
//        }
//
//        binding.listView.click {
//            howShowIt = false
//            viewModel.getStateView(false)
//        }
//        binding.tableView.click {
//            howShowIt = true
//            viewModel.getStateView(true)
//        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
    private fun initOberserver() {
//        viewModel.stateView.observe(this) {
//            howShowIt = it
//            if (it) {
//                binding.recyclerView.layoutManager = LinearLayoutManager(this)
//                binding.listView.visibility = View.VISIBLE
//                binding.tableView.visibility = View.GONE
//
//            } else {
//                binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
//                binding.listView.visibility = View.GONE
//                binding.tableView.visibility = View.VISIBLE
//            }
//        }
//        viewModel.getStateView(howShowIt)
//        viewModel.searchUserResponse.observe(this) {
//            it.let { result ->
//                try {
//                    when (result) {
//                        is UI_State.LOADING -> {
//                            binding.lyViewList.setVisibility(View.GONE);
//                            binding.shimmerViewContainer.setVisibility(View.VISIBLE);
//                            binding.shimmerViewContainer.startShimmerAnimation()
//                        }
//                        is UI_State.SUCCESS<*> -> {
//
//                            val userSearch = result.response as? List<UserSearchEntity>
//                            if (userSearch != null) {
//                                userSearch.let {
//                                    UserSearchAdapter(it).apply {
//                                        if (howShowIt)
//                                            binding.recyclerView.settingsLinearVertical(this)
//                                        else
//                                            binding.recyclerView.settingsGrid(this)
//
//                                    }
//                                    binding.lyViewList.setVisibility(View.VISIBLE);
//                                    binding.shimmerViewContainer.stopShimmerAnimation();
//                                    binding.shimmerViewContainer.setVisibility(View.GONE);
//                                }
//                            }else{
//                                dialog.showError(this, "Error at casting")
//                            }
//                        }
//                        is UI_State.ERROR -> {
//                            result.error.localizedMessage?.let { error -> toast(error) }
//                        }
//                    }
//
//                } catch (e: Exception) {
//                    dialog.showError(this,e.toString())
//
//                }
//                binding.swipeRefresh.isRefreshing = false
//            }
//        }
    }



    override fun onResume() {
        super.onResume()
//        binding.shimmerViewContainer.startShimmerAnimation()
    }

    override fun onPause() {
//        binding.shimmerViewContainer.stopShimmerAnimation()
        super.onPause()
    }

}