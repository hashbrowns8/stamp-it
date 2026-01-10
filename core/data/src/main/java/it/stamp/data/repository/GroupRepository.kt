package it.stamp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import it.stamp.data.firestore.mapper.GroupMapper
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.util.groups
import it.stamp.domain.repository.GroupRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupDataRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) : GroupRepository {

    override suspend fun findById(id: GroupId): Group? {
        return firestore.groups.document(id.value)
            .get()
            .await()
            .toObject<FirestoreGroup>()
            ?.let(GroupMapper::toDomainModel)
    }
}