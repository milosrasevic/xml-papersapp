<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rw="http://www.tim12.com/review"
>


    <xsl:template match="/">
        <html>
            <head>
                  <title>
                      Review
                  </title>
            </head>
            <body>
                <h1>Review</h1>
                <div>
                    <h4>Authors: </h4>
                    <ul>
                        <xsl:for-each select="rw:Authors">
                            <li>
                                <xsl:value-of select="rw:Author/rw:firstName"></xsl:value-of>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="rw:Author/rw:lastName"></xsl:value-of>
                                <xsl:text> (</xsl:text>
                                <xsl:value-of select="rw:Author/rw:email"></xsl:value-of>
                                <xsl:text>)</xsl:text>
                            </li>
                        </xsl:for-each>
                    </ul>
                </div>
                <div>
                    <h4>Reviewer: </h4>
                    <ul>
                        <li>
                            <xsl:value-of select="rw:Review/rw:Reviewer/rw:firstName"></xsl:value-of>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="rw:Review/rw:Reviewer/rw:lastName"></xsl:value-of>
                            <xsl:text> (</xsl:text>
                            <xsl:value-of select="rw:Review/rw:Reviewer/rw:email"></xsl:value-of>
                            <xsl:text>)</xsl:text>
                        </li>
                    </ul>
                </div>
                <div>
                    <h4>Final decision</h4>
                    <p>
                        <xsl:value-of select="rw:Review/rw:finalDecision"></xsl:value-of>
                    </p>
                </div>
                <div>
                    <h4>Overall opinion</h4>
                    <p>
                        <xsl:value-of select="rw:Review/rw:overallOpinion"></xsl:value-of>
                    </p>
                </div>
                <div>
                    <h4>Comments: </h4>
                    <ol>
                        <xsl:for-each select="rw:Review/rw:Comments/rw:Comment">
                            <li>
                                <xsl:value-of select="text()"/>
                            </li>
                        </xsl:for-each>
                    </ol>
                </div>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>