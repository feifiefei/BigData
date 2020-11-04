/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.fei.avro;

import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class SzseAvro extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 1014523571198707246L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"SzseAvro\",\"namespace\":\"com.fei.avro\",\"fields\":[{\"name\":\"mdStreamID\",\"type\":[\"string\",\"null\"]},{\"name\":\"securityID\",\"type\":[\"string\",\"null\"]},{\"name\":\"symbol\",\"type\":[\"string\",\"null\"]},{\"name\":\"tradeVolume\",\"type\":[\"long\",\"null\"]},{\"name\":\"totalValueTraded\",\"type\":[\"long\",\"null\"]},{\"name\":\"preClosePx\",\"type\":[\"double\",\"null\"]},{\"name\":\"openPrice\",\"type\":[\"double\",\"null\"]},{\"name\":\"highPrice\",\"type\":[\"double\",\"null\"]},{\"name\":\"lowPrice\",\"type\":[\"double\",\"null\"]},{\"name\":\"tradePrice\",\"type\":[\"double\",\"null\"]},{\"name\":\"closePx\",\"type\":[\"double\",\"null\"]},{\"name\":\"tradingPhaseCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"timestamp\",\"type\":[\"long\",\"null\"]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public CharSequence mdStreamID;
  @Deprecated public CharSequence securityID;
  @Deprecated public CharSequence symbol;
  @Deprecated public Long tradeVolume;
  @Deprecated public Long totalValueTraded;
  @Deprecated public Double preClosePx;
  @Deprecated public Double openPrice;
  @Deprecated public Double highPrice;
  @Deprecated public Double lowPrice;
  @Deprecated public Double tradePrice;
  @Deprecated public Double closePx;
  @Deprecated public CharSequence tradingPhaseCode;
  @Deprecated public Long timestamp;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public SzseAvro() {}

  /**
   * All-args constructor.
   * @param mdStreamID The new value for mdStreamID
   * @param securityID The new value for securityID
   * @param symbol The new value for symbol
   * @param tradeVolume The new value for tradeVolume
   * @param totalValueTraded The new value for totalValueTraded
   * @param preClosePx The new value for preClosePx
   * @param openPrice The new value for openPrice
   * @param highPrice The new value for highPrice
   * @param lowPrice The new value for lowPrice
   * @param tradePrice The new value for tradePrice
   * @param closePx The new value for closePx
   * @param tradingPhaseCode The new value for tradingPhaseCode
   * @param timestamp The new value for timestamp
   */
  public SzseAvro(CharSequence mdStreamID, CharSequence securityID, CharSequence symbol, Long tradeVolume, Long totalValueTraded, Double preClosePx, Double openPrice, Double highPrice, Double lowPrice, Double tradePrice, Double closePx, CharSequence tradingPhaseCode, Long timestamp) {
    this.mdStreamID = mdStreamID;
    this.securityID = securityID;
    this.symbol = symbol;
    this.tradeVolume = tradeVolume;
    this.totalValueTraded = totalValueTraded;
    this.preClosePx = preClosePx;
    this.openPrice = openPrice;
    this.highPrice = highPrice;
    this.lowPrice = lowPrice;
    this.tradePrice = tradePrice;
    this.closePx = closePx;
    this.tradingPhaseCode = tradingPhaseCode;
    this.timestamp = timestamp;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public Object get(int field$) {
    switch (field$) {
    case 0: return mdStreamID;
    case 1: return securityID;
    case 2: return symbol;
    case 3: return tradeVolume;
    case 4: return totalValueTraded;
    case 5: return preClosePx;
    case 6: return openPrice;
    case 7: return highPrice;
    case 8: return lowPrice;
    case 9: return tradePrice;
    case 10: return closePx;
    case 11: return tradingPhaseCode;
    case 12: return timestamp;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, Object value$) {
    switch (field$) {
    case 0: mdStreamID = (CharSequence)value$; break;
    case 1: securityID = (CharSequence)value$; break;
    case 2: symbol = (CharSequence)value$; break;
    case 3: tradeVolume = (Long)value$; break;
    case 4: totalValueTraded = (Long)value$; break;
    case 5: preClosePx = (Double)value$; break;
    case 6: openPrice = (Double)value$; break;
    case 7: highPrice = (Double)value$; break;
    case 8: lowPrice = (Double)value$; break;
    case 9: tradePrice = (Double)value$; break;
    case 10: closePx = (Double)value$; break;
    case 11: tradingPhaseCode = (CharSequence)value$; break;
    case 12: timestamp = (Long)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'mdStreamID' field.
   * @return The value of the 'mdStreamID' field.
   */
  public CharSequence getMdStreamID() {
    return mdStreamID;
  }

  /**
   * Sets the value of the 'mdStreamID' field.
   * @param value the value to set.
   */
  public void setMdStreamID(CharSequence value) {
    this.mdStreamID = value;
  }

  /**
   * Gets the value of the 'securityID' field.
   * @return The value of the 'securityID' field.
   */
  public CharSequence getSecurityID() {
    return securityID;
  }

  /**
   * Sets the value of the 'securityID' field.
   * @param value the value to set.
   */
  public void setSecurityID(CharSequence value) {
    this.securityID = value;
  }

  /**
   * Gets the value of the 'symbol' field.
   * @return The value of the 'symbol' field.
   */
  public CharSequence getSymbol() {
    return symbol;
  }

  /**
   * Sets the value of the 'symbol' field.
   * @param value the value to set.
   */
  public void setSymbol(CharSequence value) {
    this.symbol = value;
  }

  /**
   * Gets the value of the 'tradeVolume' field.
   * @return The value of the 'tradeVolume' field.
   */
  public Long getTradeVolume() {
    return tradeVolume;
  }

  /**
   * Sets the value of the 'tradeVolume' field.
   * @param value the value to set.
   */
  public void setTradeVolume(Long value) {
    this.tradeVolume = value;
  }

  /**
   * Gets the value of the 'totalValueTraded' field.
   * @return The value of the 'totalValueTraded' field.
   */
  public Long getTotalValueTraded() {
    return totalValueTraded;
  }

  /**
   * Sets the value of the 'totalValueTraded' field.
   * @param value the value to set.
   */
  public void setTotalValueTraded(Long value) {
    this.totalValueTraded = value;
  }

  /**
   * Gets the value of the 'preClosePx' field.
   * @return The value of the 'preClosePx' field.
   */
  public Double getPreClosePx() {
    return preClosePx;
  }

  /**
   * Sets the value of the 'preClosePx' field.
   * @param value the value to set.
   */
  public void setPreClosePx(Double value) {
    this.preClosePx = value;
  }

  /**
   * Gets the value of the 'openPrice' field.
   * @return The value of the 'openPrice' field.
   */
  public Double getOpenPrice() {
    return openPrice;
  }

  /**
   * Sets the value of the 'openPrice' field.
   * @param value the value to set.
   */
  public void setOpenPrice(Double value) {
    this.openPrice = value;
  }

  /**
   * Gets the value of the 'highPrice' field.
   * @return The value of the 'highPrice' field.
   */
  public Double getHighPrice() {
    return highPrice;
  }

  /**
   * Sets the value of the 'highPrice' field.
   * @param value the value to set.
   */
  public void setHighPrice(Double value) {
    this.highPrice = value;
  }

  /**
   * Gets the value of the 'lowPrice' field.
   * @return The value of the 'lowPrice' field.
   */
  public Double getLowPrice() {
    return lowPrice;
  }

  /**
   * Sets the value of the 'lowPrice' field.
   * @param value the value to set.
   */
  public void setLowPrice(Double value) {
    this.lowPrice = value;
  }

  /**
   * Gets the value of the 'tradePrice' field.
   * @return The value of the 'tradePrice' field.
   */
  public Double getTradePrice() {
    return tradePrice;
  }

  /**
   * Sets the value of the 'tradePrice' field.
   * @param value the value to set.
   */
  public void setTradePrice(Double value) {
    this.tradePrice = value;
  }

  /**
   * Gets the value of the 'closePx' field.
   * @return The value of the 'closePx' field.
   */
  public Double getClosePx() {
    return closePx;
  }

  /**
   * Sets the value of the 'closePx' field.
   * @param value the value to set.
   */
  public void setClosePx(Double value) {
    this.closePx = value;
  }

  /**
   * Gets the value of the 'tradingPhaseCode' field.
   * @return The value of the 'tradingPhaseCode' field.
   */
  public CharSequence getTradingPhaseCode() {
    return tradingPhaseCode;
  }

  /**
   * Sets the value of the 'tradingPhaseCode' field.
   * @param value the value to set.
   */
  public void setTradingPhaseCode(CharSequence value) {
    this.tradingPhaseCode = value;
  }

  /**
   * Gets the value of the 'timestamp' field.
   * @return The value of the 'timestamp' field.
   */
  public Long getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the value of the 'timestamp' field.
   * @param value the value to set.
   */
  public void setTimestamp(Long value) {
    this.timestamp = value;
  }

  /**
   * Creates a new SzseAvro RecordBuilder.
   * @return A new SzseAvro RecordBuilder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Creates a new SzseAvro RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new SzseAvro RecordBuilder
   */
  public static Builder newBuilder(Builder other) {
    return new Builder(other);
  }

  /**
   * Creates a new SzseAvro RecordBuilder by copying an existing SzseAvro instance.
   * @param other The existing instance to copy.
   * @return A new SzseAvro RecordBuilder
   */
  public static Builder newBuilder(SzseAvro other) {
    return new Builder(other);
  }

  /**
   * RecordBuilder for SzseAvro instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<SzseAvro>
    implements org.apache.avro.data.RecordBuilder<SzseAvro> {

    private CharSequence mdStreamID;
    private CharSequence securityID;
    private CharSequence symbol;
    private Long tradeVolume;
    private Long totalValueTraded;
    private Double preClosePx;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double tradePrice;
    private Double closePx;
    private CharSequence tradingPhaseCode;
    private Long timestamp;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.mdStreamID)) {
        this.mdStreamID = data().deepCopy(fields()[0].schema(), other.mdStreamID);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.securityID)) {
        this.securityID = data().deepCopy(fields()[1].schema(), other.securityID);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.symbol)) {
        this.symbol = data().deepCopy(fields()[2].schema(), other.symbol);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.tradeVolume)) {
        this.tradeVolume = data().deepCopy(fields()[3].schema(), other.tradeVolume);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.totalValueTraded)) {
        this.totalValueTraded = data().deepCopy(fields()[4].schema(), other.totalValueTraded);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.preClosePx)) {
        this.preClosePx = data().deepCopy(fields()[5].schema(), other.preClosePx);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.openPrice)) {
        this.openPrice = data().deepCopy(fields()[6].schema(), other.openPrice);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.highPrice)) {
        this.highPrice = data().deepCopy(fields()[7].schema(), other.highPrice);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.lowPrice)) {
        this.lowPrice = data().deepCopy(fields()[8].schema(), other.lowPrice);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.tradePrice)) {
        this.tradePrice = data().deepCopy(fields()[9].schema(), other.tradePrice);
        fieldSetFlags()[9] = true;
      }
      if (isValidValue(fields()[10], other.closePx)) {
        this.closePx = data().deepCopy(fields()[10].schema(), other.closePx);
        fieldSetFlags()[10] = true;
      }
      if (isValidValue(fields()[11], other.tradingPhaseCode)) {
        this.tradingPhaseCode = data().deepCopy(fields()[11].schema(), other.tradingPhaseCode);
        fieldSetFlags()[11] = true;
      }
      if (isValidValue(fields()[12], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[12].schema(), other.timestamp);
        fieldSetFlags()[12] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing SzseAvro instance
     * @param other The existing instance to copy.
     */
    private Builder(SzseAvro other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.mdStreamID)) {
        this.mdStreamID = data().deepCopy(fields()[0].schema(), other.mdStreamID);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.securityID)) {
        this.securityID = data().deepCopy(fields()[1].schema(), other.securityID);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.symbol)) {
        this.symbol = data().deepCopy(fields()[2].schema(), other.symbol);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.tradeVolume)) {
        this.tradeVolume = data().deepCopy(fields()[3].schema(), other.tradeVolume);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.totalValueTraded)) {
        this.totalValueTraded = data().deepCopy(fields()[4].schema(), other.totalValueTraded);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.preClosePx)) {
        this.preClosePx = data().deepCopy(fields()[5].schema(), other.preClosePx);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.openPrice)) {
        this.openPrice = data().deepCopy(fields()[6].schema(), other.openPrice);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.highPrice)) {
        this.highPrice = data().deepCopy(fields()[7].schema(), other.highPrice);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.lowPrice)) {
        this.lowPrice = data().deepCopy(fields()[8].schema(), other.lowPrice);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.tradePrice)) {
        this.tradePrice = data().deepCopy(fields()[9].schema(), other.tradePrice);
        fieldSetFlags()[9] = true;
      }
      if (isValidValue(fields()[10], other.closePx)) {
        this.closePx = data().deepCopy(fields()[10].schema(), other.closePx);
        fieldSetFlags()[10] = true;
      }
      if (isValidValue(fields()[11], other.tradingPhaseCode)) {
        this.tradingPhaseCode = data().deepCopy(fields()[11].schema(), other.tradingPhaseCode);
        fieldSetFlags()[11] = true;
      }
      if (isValidValue(fields()[12], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[12].schema(), other.timestamp);
        fieldSetFlags()[12] = true;
      }
    }

    /**
      * Gets the value of the 'mdStreamID' field.
      * @return The value.
      */
    public CharSequence getMdStreamID() {
      return mdStreamID;
    }

    /**
      * Sets the value of the 'mdStreamID' field.
      * @param value The value of 'mdStreamID'.
      * @return This builder.
      */
    public Builder setMdStreamID(CharSequence value) {
      validate(fields()[0], value);
      this.mdStreamID = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'mdStreamID' field has been set.
      * @return True if the 'mdStreamID' field has been set, false otherwise.
      */
    public boolean hasMdStreamID() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'mdStreamID' field.
      * @return This builder.
      */
    public Builder clearMdStreamID() {
      mdStreamID = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'securityID' field.
      * @return The value.
      */
    public CharSequence getSecurityID() {
      return securityID;
    }

    /**
      * Sets the value of the 'securityID' field.
      * @param value The value of 'securityID'.
      * @return This builder.
      */
    public Builder setSecurityID(CharSequence value) {
      validate(fields()[1], value);
      this.securityID = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'securityID' field has been set.
      * @return True if the 'securityID' field has been set, false otherwise.
      */
    public boolean hasSecurityID() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'securityID' field.
      * @return This builder.
      */
    public Builder clearSecurityID() {
      securityID = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'symbol' field.
      * @return The value.
      */
    public CharSequence getSymbol() {
      return symbol;
    }

    /**
      * Sets the value of the 'symbol' field.
      * @param value The value of 'symbol'.
      * @return This builder.
      */
    public Builder setSymbol(CharSequence value) {
      validate(fields()[2], value);
      this.symbol = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'symbol' field has been set.
      * @return True if the 'symbol' field has been set, false otherwise.
      */
    public boolean hasSymbol() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'symbol' field.
      * @return This builder.
      */
    public Builder clearSymbol() {
      symbol = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'tradeVolume' field.
      * @return The value.
      */
    public Long getTradeVolume() {
      return tradeVolume;
    }

    /**
      * Sets the value of the 'tradeVolume' field.
      * @param value The value of 'tradeVolume'.
      * @return This builder.
      */
    public Builder setTradeVolume(Long value) {
      validate(fields()[3], value);
      this.tradeVolume = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'tradeVolume' field has been set.
      * @return True if the 'tradeVolume' field has been set, false otherwise.
      */
    public boolean hasTradeVolume() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'tradeVolume' field.
      * @return This builder.
      */
    public Builder clearTradeVolume() {
      tradeVolume = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'totalValueTraded' field.
      * @return The value.
      */
    public Long getTotalValueTraded() {
      return totalValueTraded;
    }

    /**
      * Sets the value of the 'totalValueTraded' field.
      * @param value The value of 'totalValueTraded'.
      * @return This builder.
      */
    public Builder setTotalValueTraded(Long value) {
      validate(fields()[4], value);
      this.totalValueTraded = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'totalValueTraded' field has been set.
      * @return True if the 'totalValueTraded' field has been set, false otherwise.
      */
    public boolean hasTotalValueTraded() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'totalValueTraded' field.
      * @return This builder.
      */
    public Builder clearTotalValueTraded() {
      totalValueTraded = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'preClosePx' field.
      * @return The value.
      */
    public Double getPreClosePx() {
      return preClosePx;
    }

    /**
      * Sets the value of the 'preClosePx' field.
      * @param value The value of 'preClosePx'.
      * @return This builder.
      */
    public Builder setPreClosePx(Double value) {
      validate(fields()[5], value);
      this.preClosePx = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'preClosePx' field has been set.
      * @return True if the 'preClosePx' field has been set, false otherwise.
      */
    public boolean hasPreClosePx() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'preClosePx' field.
      * @return This builder.
      */
    public Builder clearPreClosePx() {
      preClosePx = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'openPrice' field.
      * @return The value.
      */
    public Double getOpenPrice() {
      return openPrice;
    }

    /**
      * Sets the value of the 'openPrice' field.
      * @param value The value of 'openPrice'.
      * @return This builder.
      */
    public Builder setOpenPrice(Double value) {
      validate(fields()[6], value);
      this.openPrice = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
      * Checks whether the 'openPrice' field has been set.
      * @return True if the 'openPrice' field has been set, false otherwise.
      */
    public boolean hasOpenPrice() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'openPrice' field.
      * @return This builder.
      */
    public Builder clearOpenPrice() {
      openPrice = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /**
      * Gets the value of the 'highPrice' field.
      * @return The value.
      */
    public Double getHighPrice() {
      return highPrice;
    }

    /**
      * Sets the value of the 'highPrice' field.
      * @param value The value of 'highPrice'.
      * @return This builder.
      */
    public Builder setHighPrice(Double value) {
      validate(fields()[7], value);
      this.highPrice = value;
      fieldSetFlags()[7] = true;
      return this;
    }

    /**
      * Checks whether the 'highPrice' field has been set.
      * @return True if the 'highPrice' field has been set, false otherwise.
      */
    public boolean hasHighPrice() {
      return fieldSetFlags()[7];
    }


    /**
      * Clears the value of the 'highPrice' field.
      * @return This builder.
      */
    public Builder clearHighPrice() {
      highPrice = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    /**
      * Gets the value of the 'lowPrice' field.
      * @return The value.
      */
    public Double getLowPrice() {
      return lowPrice;
    }

    /**
      * Sets the value of the 'lowPrice' field.
      * @param value The value of 'lowPrice'.
      * @return This builder.
      */
    public Builder setLowPrice(Double value) {
      validate(fields()[8], value);
      this.lowPrice = value;
      fieldSetFlags()[8] = true;
      return this;
    }

    /**
      * Checks whether the 'lowPrice' field has been set.
      * @return True if the 'lowPrice' field has been set, false otherwise.
      */
    public boolean hasLowPrice() {
      return fieldSetFlags()[8];
    }


    /**
      * Clears the value of the 'lowPrice' field.
      * @return This builder.
      */
    public Builder clearLowPrice() {
      lowPrice = null;
      fieldSetFlags()[8] = false;
      return this;
    }

    /**
      * Gets the value of the 'tradePrice' field.
      * @return The value.
      */
    public Double getTradePrice() {
      return tradePrice;
    }

    /**
      * Sets the value of the 'tradePrice' field.
      * @param value The value of 'tradePrice'.
      * @return This builder.
      */
    public Builder setTradePrice(Double value) {
      validate(fields()[9], value);
      this.tradePrice = value;
      fieldSetFlags()[9] = true;
      return this;
    }

    /**
      * Checks whether the 'tradePrice' field has been set.
      * @return True if the 'tradePrice' field has been set, false otherwise.
      */
    public boolean hasTradePrice() {
      return fieldSetFlags()[9];
    }


    /**
      * Clears the value of the 'tradePrice' field.
      * @return This builder.
      */
    public Builder clearTradePrice() {
      tradePrice = null;
      fieldSetFlags()[9] = false;
      return this;
    }

    /**
      * Gets the value of the 'closePx' field.
      * @return The value.
      */
    public Double getClosePx() {
      return closePx;
    }

    /**
      * Sets the value of the 'closePx' field.
      * @param value The value of 'closePx'.
      * @return This builder.
      */
    public Builder setClosePx(Double value) {
      validate(fields()[10], value);
      this.closePx = value;
      fieldSetFlags()[10] = true;
      return this;
    }

    /**
      * Checks whether the 'closePx' field has been set.
      * @return True if the 'closePx' field has been set, false otherwise.
      */
    public boolean hasClosePx() {
      return fieldSetFlags()[10];
    }


    /**
      * Clears the value of the 'closePx' field.
      * @return This builder.
      */
    public Builder clearClosePx() {
      closePx = null;
      fieldSetFlags()[10] = false;
      return this;
    }

    /**
      * Gets the value of the 'tradingPhaseCode' field.
      * @return The value.
      */
    public CharSequence getTradingPhaseCode() {
      return tradingPhaseCode;
    }

    /**
      * Sets the value of the 'tradingPhaseCode' field.
      * @param value The value of 'tradingPhaseCode'.
      * @return This builder.
      */
    public Builder setTradingPhaseCode(CharSequence value) {
      validate(fields()[11], value);
      this.tradingPhaseCode = value;
      fieldSetFlags()[11] = true;
      return this;
    }

    /**
      * Checks whether the 'tradingPhaseCode' field has been set.
      * @return True if the 'tradingPhaseCode' field has been set, false otherwise.
      */
    public boolean hasTradingPhaseCode() {
      return fieldSetFlags()[11];
    }


    /**
      * Clears the value of the 'tradingPhaseCode' field.
      * @return This builder.
      */
    public Builder clearTradingPhaseCode() {
      tradingPhaseCode = null;
      fieldSetFlags()[11] = false;
      return this;
    }

    /**
      * Gets the value of the 'timestamp' field.
      * @return The value.
      */
    public Long getTimestamp() {
      return timestamp;
    }

    /**
      * Sets the value of the 'timestamp' field.
      * @param value The value of 'timestamp'.
      * @return This builder.
      */
    public Builder setTimestamp(Long value) {
      validate(fields()[12], value);
      this.timestamp = value;
      fieldSetFlags()[12] = true;
      return this;
    }

    /**
      * Checks whether the 'timestamp' field has been set.
      * @return True if the 'timestamp' field has been set, false otherwise.
      */
    public boolean hasTimestamp() {
      return fieldSetFlags()[12];
    }


    /**
      * Clears the value of the 'timestamp' field.
      * @return This builder.
      */
    public Builder clearTimestamp() {
      timestamp = null;
      fieldSetFlags()[12] = false;
      return this;
    }

    @Override
    public SzseAvro build() {
      try {
        SzseAvro record = new SzseAvro();
        record.mdStreamID = fieldSetFlags()[0] ? this.mdStreamID : (CharSequence) defaultValue(fields()[0]);
        record.securityID = fieldSetFlags()[1] ? this.securityID : (CharSequence) defaultValue(fields()[1]);
        record.symbol = fieldSetFlags()[2] ? this.symbol : (CharSequence) defaultValue(fields()[2]);
        record.tradeVolume = fieldSetFlags()[3] ? this.tradeVolume : (Long) defaultValue(fields()[3]);
        record.totalValueTraded = fieldSetFlags()[4] ? this.totalValueTraded : (Long) defaultValue(fields()[4]);
        record.preClosePx = fieldSetFlags()[5] ? this.preClosePx : (Double) defaultValue(fields()[5]);
        record.openPrice = fieldSetFlags()[6] ? this.openPrice : (Double) defaultValue(fields()[6]);
        record.highPrice = fieldSetFlags()[7] ? this.highPrice : (Double) defaultValue(fields()[7]);
        record.lowPrice = fieldSetFlags()[8] ? this.lowPrice : (Double) defaultValue(fields()[8]);
        record.tradePrice = fieldSetFlags()[9] ? this.tradePrice : (Double) defaultValue(fields()[9]);
        record.closePx = fieldSetFlags()[10] ? this.closePx : (Double) defaultValue(fields()[10]);
        record.tradingPhaseCode = fieldSetFlags()[11] ? this.tradingPhaseCode : (CharSequence) defaultValue(fields()[11]);
        record.timestamp = fieldSetFlags()[12] ? this.timestamp : (Long) defaultValue(fields()[12]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  private static final org.apache.avro.io.DatumWriter
    WRITER$ = new org.apache.avro.specific.SpecificDatumWriter(SCHEMA$);

//  @Override public void writeExternal(java.io.ObjectOutput out)
//    throws java.io.IOException {
//    WRITER$.write(this, SpecificData.getEncoder(out));
//  }

  private static final org.apache.avro.io.DatumReader
    READER$ = new org.apache.avro.specific.SpecificDatumReader(SCHEMA$);

//  @Override public void readExternal(java.io.ObjectInput in)
//    throws java.io.IOException {
//    READER$.read(this, SpecificData.getDecoder(in));
//  }

}
