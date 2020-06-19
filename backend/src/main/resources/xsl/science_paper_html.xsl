<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:sp="http://www.tim12.com/science_paper"
    >

    <xsl:template match="sp:SciencePaper/sp:content/sp:section">
        <h4>
            <xsl:value-of select="sp:title"/>
        </h4>
            <xsl:for-each select="sp:paragraph">
                <p>
                    <xsl:value-of select="."/>
                    <div>
                        <xsl:apply-templates select="sp:picture"/>
                    </div>
                    <div>
                        <xsl:apply-templates select="sp:table"/>
                    </div>
                </p>
            </xsl:for-each>
    </xsl:template>

    <xsl:template match="sp:SciencePaper/sp:content/sp:section/sp:paragraph/sp:picture">
        <img src="@url"/>
    </xsl:template>

    <xsl:template match="sp:SciencePaper/sp:references">
        <ol style="text-align: left;">
            <xsl:for-each select="sp:Referenca">
                <li>
                    <xsl:value-of select="sp:name"/>
                </li>
            </xsl:for-each>
        </ol>
    </xsl:template>


    <xsl:template match="sp:SciencePaper/sp:content/sp:section/sp:paragraph/sp:table">
        <table style="border: solid 1px grey; display: inline-block; align-items: center;">
            <xsl:for-each select="sp:row">
                <tr style="border: solid 1px grey;">
                    <xsl:for-each select="sp:cell">
                        <td style="border: solid 1px grey;">
                            <xsl:value-of select="text()"/>
                        </td>
                    </xsl:for-each>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>

    <xsl:template match="/">
        <html>
            <head>
                <title>
                    <xsl:value-of select="sp:SciencePaper/sp:title"/>
                </title>
            </head>
            <body>
                <h1 style="text-align: center;">
                    <xsl:value-of select="sp:SciencePaper/sp:title"/>
                </h1>
                <div>
                    <ul style="text-align: center; list-style-type: none">
                        <xsl:for-each select="sp:SciencePaper/sp:authors">
                            <li style="list-style-type: none;">
                                <xsl:value-of select="sp:author/sp:firstName"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="sp:author/sp:lastName"/>
                                <xsl:text> (</xsl:text>
                                <xsl:value-of select="sp:author/sp:profession"/>
                                <xsl:text>)</xsl:text>
                            </li>
                        </xsl:for-each>
                    </ul>
                </div>
               <div>
                   <h3>
                       Abstract:
                   </h3>
                   <ul>
                       <li>
                           <xsl:text>Purpose: </xsl:text>
                           <xsl:value-of select="sp:SciencePaper/sp:abstract/sp:purpose"/>
                       </li>
                       <li>
                           <xsl:text>Findings: </xsl:text>
                           <xsl:value-of select="sp:SciencePaper/sp:abstract/sp:findings"/>
                       </li>
                       <li>
                           <xsl:text>Design and metodology aproach: </xsl:text>
                           <xsl:value-of select="sp:SciencePaper/sp:abstract/sp:designMethodologyApproach"/>
                       </li>
                       <li>
                           <xsl:text>Research limitations and Implications: </xsl:text>
                           <xsl:value-of select="sp:SciencePaper/sp:abstract/sp:researchLimitationsAndImplications"/>
                       </li>
                       <li>
                           <xsl:text>Practical implications: </xsl:text>
                           <xsl:value-of select="sp:SciencePaper/sp:abstract/sp:practicalImplications"/>
                       </li>
                       <li>
                           <xsl:text>Social implications: </xsl:text>
                           <xsl:value-of select="sp:SciencePaper/sp:abstract/sp:socialImlications"/>
                       </li>
                       <li>
                           <xsl:text>Originality and value: </xsl:text>
                           <xsl:value-of select="sp:SciencePaper/sp:abstract/sp:originalityAndValue"/>
                       </li>
                       <li>
                           <xsl:text>Keywords: </xsl:text>
                           <ol>
                               <xsl:for-each select="sp:SciencePaper/sp:abstract/sp:keywords">
                                   <xsl:value-of select="sp:keyword"/>
                                   <xsl:text> </xsl:text>
                               </xsl:for-each>
                           </ol>
                       </li>
                       <li>
                           <xsl:text>Type: </xsl:text>
                           <xsl:value-of select="sp:SciencePaper/sp:abstract/sp:type"/>
                       </li>
                   </ul>
                </div>
                <div style="text-align: center;">
                    <h3>Content</h3>
                    <xsl:apply-templates select="sp:SciencePaper/sp:content/sp:section"/>
                </div>

                <div>
                    <xsl:if test="count(sp:SciencePaper/sp:references/sp:Referenca) &gt; 0">
                        <h3 style="text-align: center;">References</h3>

                        <xsl:apply-templates select="sp:SciencePaper/sp:references"/>
                        <xsl:for-each select="sp:SciencePaper/sp:references">
                            <p>
                                <a>

                                    <xsl:attribute name="href">
                                        <xsl:text>http://localhost:8081/api/science-paper/getPDF/</xsl:text>
                                        <xsl:value-of select="sp:Referenca/sp:referenceName/sp:paperTitle"></xsl:value-of>
                                    </xsl:attribute>

                                    <xsl:attribute name="target">
                                        <xsl:text>_blank</xsl:text>
                                    </xsl:attribute>

                                    <xsl:value-of select="sp:Referenca/sp:referenceName/sp:paperTitle"></xsl:value-of>
                                </a>

                                <xsl:text> </xsl:text>
                                <xsl:for-each select="sp:Referenca/sp:referenceName/sp:authorsName">
                                    <xsl:text>, </xsl:text>
                                    <xsl:value-of select="text()"></xsl:value-of>
                                </xsl:for-each>
                            </p>
                        </xsl:for-each>
                    </xsl:if>
                </div>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>