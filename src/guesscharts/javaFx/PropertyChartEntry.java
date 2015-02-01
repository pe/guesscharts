package guesscharts.javaFx;

import guesscharts.ChartEntry;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

public class PropertyChartEntry extends ChartEntry {
	public final IntegerProperty year;
	public final IntegerProperty position;
	public final StringProperty artist;
	public final StringProperty title;
	public final StringProperty moreDetails;
	public final StringProperty audio;
	public final StringProperty cover;

	public PropertyChartEntry() {
		try {
			year = new JavaBeanIntegerPropertyBuilder().bean(this).name("year").build();
			position = new JavaBeanIntegerPropertyBuilder().bean(this).name("position").build();
			artist = new JavaBeanStringPropertyBuilder().bean(this).name("artist").build();
			title = new JavaBeanStringPropertyBuilder().bean(this).name("title").build();
			moreDetails = new JavaBeanStringPropertyBuilder().bean(this).name("moreDetails").build();
			audio = new JavaBeanStringPropertyBuilder().bean(this).name("audio").build();
			cover = new JavaBeanStringPropertyBuilder().bean(this).name("cover").build();
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
