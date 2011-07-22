package gov.usgswim.sparrow.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import gov.usgswim.sparrow.SparrowUnits;

/**
 * Domain Object representing a SPARROW SparrowModel.
 */
public interface SparrowModel {


	//Standardized names for constituents so we can detect which const. we have.
	static final String TN_CONSTITUENT_NAME = "Nitrogen";
	static final String TP_CONSTITUENT_NAME = "Phosphorus";
	static final String SEDIMENT_CONSTITUENT_NAME = "Suspended Sediment";
	
	/**
	 * The UUID for this source.  This is the database srcId.
	 * @return
	 */
	public Long getId();

	/**
	 * True if this model has been reviewed/approved by who ever might need to
	 * do such a thing.  This might include validation that the db model matches
	 * the predictions generated by the original SPARROW model.
	 * @return
	 */
	public boolean isApproved();

	/**
	 * True if this model is marked for public visibility.
	 *
	 * Some models may only be for use within the USGS.
	 * @return
	 */
	public boolean isPublic();

	/**
	 * Essentially, deletes a model by marking it as obsolete.
	 *
	 * Eventually, archived models should be deleted.
	 * @return
	 */
	public boolean isArchived();

	/**
	 * A name for the model.  Should be human readable and unique.
	 * @return
	 */
	public String getName();

	/**
	 * A description of the model.
	 * @return
	 */
	public String getDescription();

	/**
	 * A URL associated with the model where more information can be found.
	 * @return
	 */
	public String getUrl();

	/**
	 * Date imported into the database.
	 * @return
	 */
	public Date getDateAdded();

	/**
	 * ID in the CONTACT table for contact info.
	 * @return
	 */
	public Long getContactId();

	/**
	 * ID in the STREAM_NETWORK.ENH_NETWORK table for the network this model is
	 * based on.
	 * @return
	 */
	public Long getEnhNetworkId();
	
	/**
	 * Returns the MapViewer theme to use for the geometry.  This will likely
	 * be a cached predefined theme either for just this model or for a set of
	 * models that share the same geometry.
	 * @return
	 */
	public String getThemeName();

	/**
	 * Latitude of the Northern bound of the model area.
	 * @return
	 */
	public Double getNorthBound();

	/**
	 * Longitude of the Eastern bound of the model area.
	 * @return
	 */
	public Double getEastBound();

	/**
	 * Latitude of the Southern bound of the model area.
	 * @return
	 */
	public Double getSouthBound();

	/**
	 * Longitude of the Western bound of the model area.
	 * @return
	 */
	public Double getWestBound();

	/**
	 * Constituent of the model.
	 *
	 * @return
	 */
	public String getConstituent();
	
	/**
	 * Returns the detection limit for the specified dataseries.
	 * 
	 * Currently this only affects the concentration dataseries, which has
	 * different detection limits for each constituent.
	 * 
	 * @return
	 */
	public BigDecimal getDetectionLimit(DataSeriesType dataSeries, ComparisonType comparisonType);
	
	/**
	 * Returns the max number of decimal places that should be used for this
	 * data series for this model.
	 * 
	 * Note that percentage comparisons should use a separate standard.
	 * 
	 * @param dataSeries
	 * @return If null, there is no recommendation.
	 */
	public Integer getMaxDecimalPlaces(DataSeriesType dataSeries, ComparisonType comparisonType);

	/**
	 * Units of the model.
	 *
	 * @return
	 */
	public SparrowUnits getUnits();
	
	/**
	 * Returns a list of saved sessions associated to this model.
	 *
	 * @return
	 */
	public List<IPredefinedSession> getSessions();


	/**
	 * Returns a sorted list of model Source instances.
	 *
	 * Sorting is based on the Source's sort order.
	 * If there are no sources, this method should return an empty list.
	 * @return
	 */
	public List<Source> getSources();

	/**
	 * Returns a source from the model based on its Identifier.
	 *
	 * Identifiers are usually numbered sequential for sources in a model, ie.,
	 * the first source in a model has identifier #1.  Thus, identifiers are not
	 * UUIDs, so they cannot be used to uniquely identify a source in the db.
	 *
	 * If the identifier is not found, null is returned.
	 *
	 * @param identifier
	 * @return
	 */
	public Source getSource(int identifier);
	
}
