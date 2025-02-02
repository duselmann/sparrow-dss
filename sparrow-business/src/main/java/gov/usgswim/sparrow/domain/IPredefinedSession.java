package gov.usgswim.sparrow.domain;

import java.io.Serializable;
import java.sql.Date;

public interface IPredefinedSession extends Serializable {

	/**
	 * @return the id
	 */
	public Long getId();

	/**
	 * @return the uniqueCode
	 */
	public String getUniqueCode();

	/**
	 * @return the modelId
	 */
	public Long getModelId();

	/**
	 * @return the predefinedSessionType
	 */
	public PredefinedSessionType getPredefinedSessionType();

	/**
	 * @return the approved
	 */
	public Boolean getApproved();

	/**
	 * @return the name
	 */
	public String getName();

	/**
	 * @return the description
	 */
	public String getDescription();

	/**
	 * @return the sortOrder
	 */
	public Integer getSortOrder();

	/**
	 * @return the contextString
	 */
	public String getContextString();

	/**
	 * @return the addDate
	 */
	public Date getAddDate();

	/**
	 * @return the addBy
	 */
	public String getAddBy();

	/**
	 * @return the addNote
	 */
	public String getAddNote();

	/**
	 * @return the addContactInfo
	 */
	public String getAddContactInfo();

	/**
	 * @return the customCategory
	 */
	public String getGroupName();
	
	/**
	 * @return the topic
	 */
	public PredefinedSessionTopic getTopic();

}