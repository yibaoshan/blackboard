package cn.ybs.recyclerview

import cn.ybs.core.base.BaseViewBindingActivity
import cn.ybs.recyclerview.databinding.ActivityMainBinding

/**
 * author : <a href="https://yibs.space"/>
 * e-mail : yibaoshan@foxmail.com
 * time   : 2024/05/19
 * desc   : RV 导航页
 */
class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    override fun initListenersWhenInitViews() {
        val view = viewBinding ?: return
        view.tvNormalLinearLayoutVerticalTextType.setOnClickListener { AppRouter.startTextVerticalLinearLayoutNormalRecyclerViewActivity(this) }
        view.tvNormalLinearLayoutVerticalImageType.setOnClickListener { AppRouter.startImageVerticalLinearLayoutNormalRecyclerViewActivity(this) }
        view.tvNormalLinearLayoutVerticalMultiType.setOnClickListener { AppRouter.startMultiTypeVerticalLinearLayoutNormalRecyclerViewActivity(this) }

        view.tvNormalGridLayoutVerticalTextType.setOnClickListener { AppRouter.startTextVerticalGridLayoutNormalRecyclerViewActivity(this) }
        view.tvNormalStaggeredGridLayoutVerticalMultiType.setOnClickListener { AppRouter.startMultiTypeVerticalStaggeredGridLayoutNormalRecyclerViewActivity(this) }
        view.tvNormalFlexboxLayoutVerticalTextType.setOnClickListener { AppRouter.startTextVerticalFlexboxLayoutNormalRecyclerViewActivity(this) }
    }

}