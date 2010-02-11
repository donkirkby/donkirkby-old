<xsl:transform version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" version="4.0" indent="yes"/>

  <!-- This extracts the number of cards in each category with a "- Hanzi" ending 
  	add a transformation instruction to the top of the xml file like this:
  	<?xml-stylesheet type="text/xsl" href="category-count.xsl"?>
  	Then open the file in a browser.
  	-->
  <xsl:template match="*">
	<table>
		<xsl:apply-templates/>
	</table>
  </xsl:template>
  <xsl:template match="/*/*"/>
  <xsl:template match="/*/category[substring(name, string-length(name) - 6, string-length(name)) = '- Hanzi']">
	<tr>
	<td><xsl:value-of select="name"/></td><td><xsl:value-of select="count(/*/item[cat=current()/name])"/></td>	 
	</tr>
  </xsl:template>
</xsl:transform>
