package com.example.a5150_courier.Common

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.a5150_courier.Callback.DelButton
import java.util.LinkedList
import java.util.Queue

abstract class Swipe (context:Context , private val recyclerView:RecyclerView, internal var buttonWidth:Int):ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
    private var buttonList:MutableList<RemoveItemButton>?=null
    private lateinit var gesture:GestureDetector
    private var swipePosition = -1
    private var swipeThreshold = 0.5f
    private val buttonBuffer:MutableMap<Int,MutableList<RemoveItemButton>>
    private lateinit var removeQueue:Queue<Int>

    private val gestListener = object:GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            for (button in buttonList!!)
                if (button.onClick(e.x,e.y))
                    break
            return true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener{ _, motionEvent ->
        if (swipePosition < 0) return@OnTouchListener false
        val point = Point(motionEvent.rawX.toInt(),motionEvent.rawY.toInt())

        val swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition)
        val swipedItem = swipeViewHolder!!.itemView
        val rect=Rect()
        swipedItem.getGlobalVisibleRect(rect)

        if (motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_UP || motionEvent.action == MotionEvent.ACTION_MOVE){
            if (rect.top < point.y && rect.bottom > point.y)
                gesture.onTouchEvent(motionEvent)
            else{
            removeQueue.add(swipePosition)
            swipePosition = -1
            recoverSwipe()
        }

    }
        false
    }

    private fun recoverSwipe() {
        while (!removeQueue.isEmpty()){
            val pos = removeQueue.poll()!!
            if(pos > -1)
                recyclerView.adapter!!.notifyItemChanged(pos)
        }
    }

    init {
        this.buttonList = ArrayList()
        this.gesture = GestureDetector(context,gestListener)
        this.recyclerView.setOnTouchListener(onTouchListener)
        this.buttonBuffer = HashMap()

        removeQueue = object:LinkedList<Int>(){
            override fun add(element: Int): Boolean {
                return if (contains(element))
                    false
                else
                    super.add(element)
            }
        }
        attachSwipe()
    }

    private fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    inner class RemoveItemButton(private val context: Context, private val text:String, private val textSize:Int, private val imageResId:Int, private val color:Int, private val listener:DelButton){
        private var pos:Int=0
        private var clickReg : RectF?=null
        private val resources:Resources

        init{
            resources = context.resources
        }
        fun onClick(x:Float, y:Float):Boolean{
            if (clickReg !=null && clickReg!!.contains(x,y)){
                listener.onClick(pos)
                return true
            }
            return false
        }

        fun onDraw(c: Canvas, rectF: RectF, pos:Int){
            val p= Paint()
            p.color = color
            c.drawRect(rectF,p)
            p.color = Color.rgb(188, 170, 164)
            p.textSize = textSize.toFloat()
            val r= Rect()
            val height = rectF.height()
            val width = rectF.width()
            p.textAlign = Paint.Align.LEFT
            p.getTextBounds(text, 0,text.length,r)
            var x = 0f
            var y = 0f
            if (imageResId == 0){
                x = width/2f - r.width() / 2f - r.left.toFloat()
                y = height/2f + r.height() / 2f - r.bottom
                c.drawText(text,rectF.left+x, rectF.top+y,p)
            }else{
                val d = ContextCompat.getDrawable(context,imageResId)
                val bitmap = drawableToBitmap(d)
                c.drawBitmap(bitmap, (rectF.left + rectF.right)/2, rectF.top+rectF.bottom/2,p)
            }
            clickReg = rectF
            this.pos = pos
        }

    }

    private fun drawableToBitmap(d: Drawable?): Bitmap {
        if (d!! is BitmapDrawable)
            return d.toBitmap()
        val bitmap = Bitmap.createBitmap(d!!.intrinsicWidth,d.intrinsicHeight,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        d.setBounds(0,0,canvas.width,canvas.height)
        d.draw(canvas)
        return bitmap
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipePosition != pos)
            removeQueue.add(swipePosition)
        swipePosition = pos
        if (buttonBuffer.containsKey(swipePosition))
            buttonList = buttonBuffer[swipePosition]
        else
            buttonList!!.clear()
        buttonBuffer.clear()
        swipeThreshold = 0.5f*buttonList!!.size.toFloat()*buttonWidth.toFloat()
        recoverSwipe()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f*defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f*defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var tralationX = dX
        var  itemView = viewHolder.itemView
        if (pos < 0){
            swipePosition = pos
            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if (dX < 0){
                var buffer : MutableList<RemoveItemButton> = ArrayList()
                if (!buttonBuffer.containsKey(pos)){
                    instantiateRemoveItemButton(viewHolder,buffer)
                    buttonBuffer[pos] = buffer
                }else
                    buffer = buttonBuffer[pos]!!
                tralationX = dX*buffer.size.toFloat()*buttonWidth.toFloat()/itemView.width
                drawButton(c,itemView,buffer,pos,tralationX)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, tralationX, dY, actionState, isCurrentlyActive)
    }

    abstract fun instantiateRemoveItemButton(viewHolder: RecyclerView.ViewHolder, buffer: MutableList<Swipe.RemoveItemButton>)

    private fun drawButton(c: Canvas, itemView: View, buffer: MutableList<Swipe.RemoveItemButton>, pos: Int, tralationX: Float) {
        var right = itemView.right.toFloat()
        val dButtonWidth = -1*tralationX/buffer.size
        for (button in buffer){
            val left = right - dButtonWidth
            button.onDraw(c, RectF(left,itemView.top.toFloat(),right,itemView.bottom.toFloat()),pos)
            right = left
        }
    }
}