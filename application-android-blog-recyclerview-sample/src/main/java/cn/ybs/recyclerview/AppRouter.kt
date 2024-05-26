package cn.ybs.recyclerview

import android.content.Context
import android.content.Intent
import cn.ybs.recyclerview.constans.Intents
import cn.ybs.recyclerview.ui.basic.BasicExampleOfUsingRecyclerViewDetailActivity
import cn.ybs.recyclerview.ui.basic.BasicExampleOfUsingRecyclerViewGuideActivity

/**
 * author : <a href="https://yibs.space"/>
 * e-mail : yibaoshan@foxmail.com
 * time   : 2024/05/19
 * desc   : 临时路由导航类
 */
object AppRouter {

    object Basic {

        fun startBasicUsageActivity(context: Context) {
            val intent = Intent(context, BasicExampleOfUsingRecyclerViewGuideActivity::class.java)
            context.startActivity(intent)
        }

        fun startTextVerticalLinearLayoutRecyclerViewActivity(context: Context) {
            startBasicUsageRecyclerViewActivity(context, Intents.INTENT_VALUE_VERTICAL_LINEAR_LAYOUT_TEXT)
        }

        fun startImageVerticalLinearLayoutRecyclerViewActivity(context: Context) {
            startBasicUsageRecyclerViewActivity(context, Intents.INTENT_VALUE_VERTICAL_LINEAR_LAYOUT_IMAGE)
        }

        fun startMultiTypeVerticalLinearLayoutRecyclerViewActivity(context: Context) {
            startBasicUsageRecyclerViewActivity(context, Intents.INTENT_VALUE_VERTICAL_LINEAR_LAYOUT_MULTI)
        }

        fun startTextVerticalGridLayoutRecyclerViewActivity(context: Context) {
            startBasicUsageRecyclerViewActivity(context, Intents.INTENT_VALUE_VERTICAL_GRID_LAYOUT_TEXT)
        }

        fun startMultiTypeVerticalStaggeredGridLayoutRecyclerViewActivity(context: Context) {
            startBasicUsageRecyclerViewActivity(context, Intents.INTENT_VALUE_VERTICAL_STAGGERED_LAYOUT_TEXT)
        }

        fun startTextVerticalFlexboxLayoutRecyclerViewActivity(context: Context) {
            startBasicUsageRecyclerViewActivity(context, Intents.INTENT_VALUE_VERTICAL_FLEXBOX_LAYOUT_TEXT)
        }

        private fun startBasicUsageRecyclerViewActivity(context: Context, type: String) {
            val intent = Intent(context, BasicExampleOfUsingRecyclerViewDetailActivity::class.java)
            intent.putExtra(Intents.INTENT_KEY_RECYCLER_VIEW_TYPE, type)
            context.startActivity(intent)
        }

    }

}