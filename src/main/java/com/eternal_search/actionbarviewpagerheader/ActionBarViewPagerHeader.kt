package com.eternal_search.actionbarviewpagerheader

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.view_actionbar_viewpager_header.view.*

class ActionBarViewPagerHeader(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
	FrameLayout(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener {
	
	private var viewPager: ViewPager? = null
	private var curPageTitleIndex: Int = -1
	private var nextPageTitleIndex: Int = -1
	private var pageScrollDelta: Int? = null
	
	constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
	
	constructor(context: Context): this(context, null)
	
	init {
		LayoutInflater.from(context).inflate(R.layout.view_actionbar_viewpager_header, this)
		actionbar_go_forward_button.setOnClickListener {
			val viewPager = viewPager ?: return@setOnClickListener
			if (viewPager.currentItem < ((viewPager.adapter?.count ?: 0) - 1)) {
				viewPager.currentItem++
			}
		}
		actionbar_go_backward_button.setOnClickListener {
			val viewPager = viewPager ?: return@setOnClickListener
			if (viewPager.currentItem > 0) {
				viewPager.currentItem--
			}
		}
		actionbar_title_container.setOnClickListener {
			performClick()
		}
	}
	
	fun attachToViewPager(viewPager: ViewPager) {
		if (this.viewPager != null) {
			throw IllegalStateException("ActionBarViewPagerHeader already attached to ViewPager")
		}
		this.viewPager = viewPager
		onPageSelected(viewPager.currentItem)
		update()
		viewPager.addOnPageChangeListener(this)
	}
	
	override fun onPageScrollStateChanged(state: Int) {
		when (state) {
			ViewPager.SCROLL_STATE_DRAGGING -> {
				actionbar_go_forward_button.animate().alpha(0.4f).start()
				actionbar_go_backward_button.animate().alpha(0.4f).start()
				pageScrollDelta = 0
			}
			ViewPager.SCROLL_STATE_SETTLING -> {
				actionbar_go_forward_button.animate().alpha(1.0f).start()
				actionbar_go_backward_button.animate().alpha(1.0f).start()
			}
			ViewPager.SCROLL_STATE_IDLE -> {
				actionbar_next_title_container.visibility = View.GONE
				curPageTitleIndex = -1
				nextPageTitleIndex = -1
			}
		}
	}
	
	override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
		updateTitles(if (positionOffset < 0.9f) position else position + 1, -positionOffset)
		updateNextTitles(position + 1, 1.0f - positionOffset)
		if (pageScrollDelta == 0) {
			pageScrollDelta = if (positionOffset > 0.5f) -1 else 1
			actionbar_next_title_container.visibility = View.VISIBLE
		}
	}
	
	override fun onPageSelected(position: Int) {
		actionbar_go_forward_button.visibility = if (position < ((viewPager?.adapter?.count ?: 0) - 1))
			View.VISIBLE
		else
			View.INVISIBLE
		actionbar_go_backward_button.visibility = if (position > 0)
			View.VISIBLE
		else
			View.INVISIBLE
	}
	
	private fun update() {
		val viewPager = viewPager ?: return
		if (viewPager.currentItem >= 0) {
			updateTitles(viewPager.currentItem, 0.0f)
		}
	}
	
	private fun updateTitles(position: Int, offset: Float) {
		val viewPager = viewPager ?: return
		if (curPageTitleIndex != position) {
			val titles = viewPager.adapter?.getPageTitle(position)?.split('\n', limit = 2)
			actionbar_title.text = titles?.first()
			actionbar_subtitle.text = titles?.lastOrNull()
			actionbar_subtitle.visibility = if (titles?.size == 2)
				View.VISIBLE
			else
				View.GONE
			curPageTitleIndex = position
		}
		actionbar_title_container.translationX = offset * width
	}
	
	private fun updateNextTitles(position: Int, offset: Float) {
		val viewPager = viewPager ?: return
		if (nextPageTitleIndex != position) {
			val titles = viewPager.adapter?.getPageTitle(position)?.split('\n', limit = 2)
			actionbar_next_title.text = titles?.first()
			actionbar_next_subtitle.text = titles?.lastOrNull()
			actionbar_next_subtitle.visibility = if (titles?.size == 2)
				View.VISIBLE
			else
				View.GONE
			nextPageTitleIndex = position
		}
		actionbar_next_title_container.translationX = offset * width
	}
	
	companion object {
		@JvmStatic
		fun create(inflater: LayoutInflater, parent: ViewGroup): ActionBarViewPagerHeader =
			inflater.inflate(R.layout.toolbar_viewpager_header, parent, false) as ActionBarViewPagerHeader
	}
}
