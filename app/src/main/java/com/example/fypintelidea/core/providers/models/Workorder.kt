package com.example.fypintelidea.core.providers.models

import android.os.Parcelable
import com.connectavo.app.connectavo_android.core.services.ConnectavoBaseApiResponse
import com.example.fypintelidea.core.providers.models.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Created by ibtisam on 8/8/2018.
 */
@Parcelize
class Workorder(
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    @SerializedName("id")
    @Expose(serialize = false)
    var id: String? = null,

    @SerializedName("_id")
    var search_id: String? = null,

    @SerializedName("id_spartacus")
    @Expose
    var id_spartacus: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("asset_name")
    @Expose(serialize = false)
    var asset_name: String? = null,

    @SerializedName("assignee")
    @Expose(serialize = false)
    var assignee: String? = null,

    @SerializedName("due_date")
    @Expose
    var due_date: String? = null,

    @SerializedName("a_type")
    @Expose
    var a_type: String? = null,

    @SerializedName("priority")
    @Expose
    var priority: String? = null,

    @SerializedName("category")
    @Expose
    var category: String? = null,

    @SerializedName("created_at")
    @Expose(serialize = false)
    var created_at: String? = null,

    @SerializedName("sequence_id")
    @Expose(serialize = false)
    var sequence_id: String? = null,

    @SerializedName("eta")
    @Expose
    var eta: String? = null,

    @SerializedName("description")
    @Expose
    var description: String? = null,

    @SerializedName("tags")
    @Expose(serialize = false)
    var tags: List<Tag>? = null,

    @SerializedName("documents")
    @Expose
    var documents: ArrayList<Document>? = null,

    @SerializedName("wo_checklists_attributes")
    @Expose
    var wo_checklists_attributes: List<Checklist>? = null,

    @SerializedName("asset_id")
    @Expose
    var asset_id: String? = null,

    @SerializedName("modified_by")
    @Expose
    var modified_by: String? = null,

    @SerializedName("company_id")
    @Expose
    var company_id: String? = null,

    @SerializedName("hashed")
    @Expose(deserialize = false)
    var hashed: String? = null,

    @SerializedName("assigned_to")
    @Expose
    var assigned_to: Assigned_to? = null,

    @Expose(deserialize = false)
    @SerializedName("requested_by")
    var requested_by: String? = null,

    @SerializedName("tag_ids")
    @Expose(deserialize = false)
    var tag_ids: List<String>? = null,

    @Expose
    @SerializedName("start_time")
    var start_time: String? = null,

    @Expose
    @SerializedName("end_time")
    var end_time: String? = null,

    @Expose
    @SerializedName("hours_spent")
    var hours_spent: String? = null,

    @SerializedName("problem")
    @Expose
    var problem: String? = null,

    @SerializedName("solution")
    @Expose
    var solution: String? = null,

    @SerializedName("comment")
    @Expose
    var comment: String? = null,

    @Expose
    @SerializedName("decline_reason")
    var decline_reason: String? = null,

    @SerializedName("team_ids")
    @Expose
    var teamIds: List<String>? = null,

    @SerializedName("contributors")
    @Expose
    var contributors: ArrayList<User>? = null,

    @SerializedName("costs")
    @Expose
    var costs: List<Costs>? = null,

    @SerializedName("spare_parts")
    @Expose(serialize = false)
    var spareParts: ArrayList<SpareParts>? = null,

    @SerializedName("custom_fields")
    @Expose
    var customFields: List<CustomField>? = null,

    @SerializedName("chklist_sections")
    @Expose(serialize = false)
    var chklistSections: List<ChklistSection>? = null,

    @SerializedName("chklist_id")
    @Expose
    var chklist_id: String? = null,

    @SerializedName("marked_as_done_by")
    @Expose
    var markedAsDoneBy: String? = null,

    ) : ConnectavoBaseApiResponse(), Serializable, Parcelable {

    companion object {
        //Possible values ['pending', 'doing', 'done']
        const val WORKORDER_STATUS_REQUESTED = "requested"
        const val WORKORDER_STATUS_OPEN = "pending"
        const val WORKORDER_STATUS_CANCELED = "cancelled"
        const val WORKORDER_STATUS_INPROGRESS = "doing"
        const val WORKORDER_STATUS_DONE = "done"
        const val WORKORDER_STATUS_PAUSED = "paused"

        //Possible values ['low', 'medium', 'high']
        const val WORKORDER_PRIORITY_LOW = "low"
        const val WORKORDER_PRIORITY_MEDIUM = "medium"
        const val WORKORDER_PRIORITY_HIGH = "high"
        const val WORKORDER_TYPE_PLANNED = "planned"
        const val WORKORDER_TYPE_UNPLANNED = "unplanned"
        const val WORKORDER_TYPE_PREDICTIVE = "predictive"
    }
}