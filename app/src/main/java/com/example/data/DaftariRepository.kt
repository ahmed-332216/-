package com.example.data

import kotlinx.coroutines.flow.Flow

class DaftariRepository(private val dao: DaftariDao) {
    fun getAllEntries(): Flow<List<DaftariEntry>> = dao.getAllEntriesFlow()
    fun getEntriesByCategory(category: String): Flow<List<DaftariEntry>> = dao.getEntriesByCategoryFlow(category)
    suspend fun getEntriesByCategoryList(category: String): List<DaftariEntry> = dao.getEntriesByCategory(category)
    suspend fun insert(entry: DaftariEntry) = dao.insertEntry(entry)
    suspend fun update(entry: DaftariEntry) = dao.updateEntry(entry)
    suspend fun delete(entry: DaftariEntry) = dao.deleteEntry(entry)
    suspend fun deleteById(id: Int) = dao.deleteEntryById(id)
}
