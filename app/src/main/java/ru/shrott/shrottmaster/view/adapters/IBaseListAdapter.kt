package ru.shrott.shrottmaster.view.adapters

interface IBaseListAdapter<T> {
    fun add(newItem: T)
    fun add(newItems: ArrayList<T>?)
    fun replaceItems(newItems: List<T>)
    fun addAtPosition(pos : Int, newItem : T)
    fun remove(position: Int)
    fun clearAll()
}