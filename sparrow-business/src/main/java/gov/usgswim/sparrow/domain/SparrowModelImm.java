package gov.usgswim.sparrow.domain;

import gov.usgswim.Immutable;
import gov.usgswim.sparrow.SparrowUnits;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Immutable implementation of SparrowModel, which is a Domain Object representing a SPARROW SparrowModel.
 */
@Immutable
public class SparrowModelImm implements SparrowModel, Serializable {

	private static final long serialVersionUID = 4L;
	
	private final Long _id;
	private final boolean _approved;
	private final boolean _public;
	private final boolean _archived;
	private final String _name;
	private final String _description;
	private final String _url;
	private final Date _dateAdded;
	private final Long _contactId;
	private final Long _enhNetworkId;
	private final String _enhNetworkName;
	private final String _enhNetworkUrl;
	private final String _enhNetworkIdColumn;
	private final String _themeName;
	private final Double _northBound;
	private final Double _eastBound;
	private final Double _southBound;
	private final Double _westBound;
	private final String _constituent;
	private final boolean _usingSimpleReachIds;
	private final SparrowUnits _units;
	private final List<Source> _sources;
	private final List<IPredefinedSession> _sessions;
	private final List<String> _states;
	private final List<String> _regions;
	private final boolean _isNational;
	private final int _baseYear;

	/*
	private SparrowModelImm() {
		//some tools need a no-arg constructor.  Not really usable w/o reflection.
	}
	*/

	/**
	 * Constructs an immutable SparrowModel instance.
	 * This constructor does not perform deep copies of mutable parameters.
	 * This constructor is meant to be accessed by factories or builders in
	 * the same package. Those classes, not this class, bear the 
	 * responsibility for deep-copying the lists to ensure that an instance
	 * is truly immutable.
	 *
	 * @param id
	 * @param approved
	 * @param isPublic
	 * @param archived
	 * @param name
	 * @param description
	 * @param url
	 * @param dateAdded
	 * @param contactId
	 * @param enhNetworkId
	 * @param northBound
	 * @param eastBound
	 * @param southBound
	 * @param westBound
	 * @param constituent
	 * @param units
	 * @param sessions
	 * @param sources
	 */
	protected SparrowModelImm(Long id, boolean approved, boolean isPublic, boolean archived,
				String name, String description, String url, Date dateAdded,
				Long contactId, Long enhNetworkId, String enhNetworkName, String enhNetworkUrl, String enhNetworkIdColumn,
				String themeName,
				Double northBound, Double eastBound, Double southBound, Double westBound,
				String constituent, boolean usingSimpleReachIds, SparrowUnits units,
				List<IPredefinedSession> sessions, List<Source> sources, boolean isNational, int baseYear, 
				List<String> states, List<String> regions) {

		_id = id;
		_approved = approved;
		_public = isPublic;
		_archived = archived;
		_name = name;
		_description = description;
		_url = url;
		_contactId = contactId;
		_enhNetworkId = enhNetworkId;
		_enhNetworkName = enhNetworkName;
		_enhNetworkUrl = enhNetworkUrl;
		_enhNetworkIdColumn = enhNetworkIdColumn;
		_themeName = themeName;
		_northBound = northBound;
		_eastBound = eastBound;
		_southBound = southBound;
		_westBound = westBound;
		_constituent = constituent;
		_usingSimpleReachIds = usingSimpleReachIds;
		_units = units;
		_isNational = isNational;
		_baseYear = baseYear;
		_dateAdded = dateAdded;
		_sessions = sessions;
		_sources = sources;
		_regions = regions;
		_states = states;
	}

	public Long getId() {return _id;}

	@Override
	public boolean isApproved() {return _approved;}

	@Override
	public boolean isPublic() {return _public;}

	@Override
	public boolean isArchived() {return _archived;}

	@Override
	public String getName() {return _name;}

	@Override
	public String getDescription() {return _description;}

	@Override
	public String getUrl() {return _url;}

	@Override
	public Date getDateAdded() { return _dateAdded;}

	@Override
	public Long getContactId() { return _contactId;}

	@Override
	public Long getEnhNetworkId() { return _enhNetworkId;}
	
	@Override
	public String getEnhNetworkName() { return _enhNetworkName; }

	@Override
	public String getEnhNetworkUrl() { return _enhNetworkUrl; }
	
	@Override
	public String getEnhNetworkIdColumn() { return _enhNetworkIdColumn; }

	@Override
	public String getThemeName() { return _themeName; }

	@Override
	public Double getNorthBound() { return _northBound;}

	@Override
	public Double getEastBound() { return _eastBound;}

	@Override
	public Double getSouthBound() { return _southBound;}

	@Override
	public Double getWestBound() { return _westBound;}

	@Override
	public String getConstituent() { return _constituent;}
	
	@Override
	public boolean isUsingSimpleReachIds() { return _usingSimpleReachIds; }
	
	@Override
	public SparrowUnits getUnits() { return _units;}

	@Override
	public List<IPredefinedSession> getSessions() { return _sessions;}

	@Override
	public List<Source> getSources() { return _sources;}

	@Override
	public BigDecimal getDetectionLimit(DataSeriesType dataSeries, ComparisonType comparisonType) {
		return getDetectionLimit(dataSeries, _constituent, comparisonType);
	}
	
	@Override
	public Integer getMaxDecimalPlaces(DataSeriesType dataSeries, ComparisonType comparisonType) {
		return getMaxDecimalPlaces(dataSeries, getDetectionLimit(dataSeries, _constituent), comparisonType);
	}
	
	@Override
	public Source getSourceById(int identifier) {
		if (_sources != null) {

			for (int i = 0; i < _sources.size(); i++)  {
				Source s = _sources.get(i);
				if (s.getIdentifier() == identifier) return s;
			}

			return null;	//not found
		}
		return null;	//no sources
	}
	
	@Override
	public Source getSourceByIndex(int index) {
		return _sources.get(index);
	}
	
	/**
	 * Returns the min value to display (detection limit) for this model.
	 * 
	 * This method ignores the comparison type so that we get what the nominal
	 * detection limit would be.
	 * 
	 * @param dataSeries
	 * @param constituent
	 * @return
	 */
	public static BigDecimal getDetectionLimit(DataSeriesType dataSeries, String constituent) {
		switch (dataSeries) {
		case total_concentration:
			
			ConstituentType type = ConstituentType.SUSPENDED_SEDIMENT.fromStringIgnoreCase(constituent);
			
			if (type != null) {
				return type.getConcentrationDetectionLimit();
			} else {
				throw new RuntimeException("Unrecognized constituent '" + constituent + "'");
			}

		default:
			return null;
		}
	}
	
	/**
	 * Returns the min value to display (detection limit) for this model.
	 * 
	 * This method ignores the comparison type so that we get what the nominal
	 * detection limit would be.
	 * 
	 * @param dataSeries
	 * @param constituent
	 * @return
	 */
	public static BigDecimal getDetectionLimit(DataSeriesType dataSeries, String constituent, ComparisonType comparisonType) {
		if (comparisonType.equals(ComparisonType.none)) {
			return getDetectionLimit(dataSeries, constituent);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the max decimal places allowed to be displayed for this data
	 * series, detection limit, and comparison type.
	 * 
	 * @param dataSeries
	 * @param detectionLimit
	 * @param comparisonType
	 * @return
	 */
	public static Integer getMaxDecimalPlaces(DataSeriesType dataSeries, BigDecimal detectionLimit, ComparisonType comparisonType) {
		
		if (! comparisonType.equals(ComparisonType.none) || comparisonType.equals(ComparisonType.percent_change)) {
			return 0;	//Percentage comparisons should have zero decimal places
		} else if (! comparisonType.equals(ComparisonType.none)) {
			return null;	//No max decimal places for absolute comparisons
		}
		
		Integer decimalPlaces = null;
		
		switch (dataSeries) {
		case total:
		case decayed_incremental:
			decimalPlaces = 0;
			break;
		case total_concentration:
			
			if (detectionLimit != null) {
				decimalPlaces = getNonZeroDecimalPlaces(detectionLimit);
				if (decimalPlaces < 0) decimalPlaces = 0;
			}
			
			break;
		case incremental_yield:
		case total_yield:
			//No specific decimal places
			break;
		case source_value:
			decimalPlaces = 1;
			break;
		case flux:
			//Use actual
			break;
		case total_std_error_estimate:
		case incremental_std_error_estimate:
			decimalPlaces = 0;
			break;
		case incremental_delivered_flux:
		case total_delivered_flux:
			decimalPlaces = 0;
			break;
		case delivered_fraction:
			decimalPlaces = 2;
			break;
		case incremental_delivered_yield:
			//No specific decimal places
			break;
		default:
			//OK if other internal dataseries are used w/o a specific
			//decimal place setting.
		}
		
		return decimalPlaces;
	}
	
	/**
	 * Returns the number of decimal places in the number, ignoring any trailing
	 * zeros.
	 * 
	 * If the return value is negative, the number has no decimal places and
	 * has zeros to the right of the decimal.  For instance:
	 * 954300 would return a negative number in accordance w/ the BigDecimal
	 * definition of scale.
	 * 
	 * @param value
	 * @return
	 */
	protected static int getNonZeroDecimalPlaces(BigDecimal value) {
		return value.stripTrailingZeros().scale();
	}

	@Override
	public boolean isNational() {
		return _isNational;
	}

	@Override
	public int getBaseYear() {
		return _baseYear;
	}

	@Override
	public List<String> getStates() {
		return _states;
	}

	@Override
	public List<String> getRegions() {
		return _regions;
	}

}
