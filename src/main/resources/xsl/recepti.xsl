<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:recepti="http://www.zis.rs/seme/recepti"
                xmlns:recept="http://www.zis.rs/seme/recept"
                xmlns:zko="http://www.zis.rs/seme/zdravstveni_karton">

    <xsl:template match="/">

        <html>
            <head>
                <style type="text/css">
                    span {
                    display:table;
                    margin:0 auto;
                    }
                    div {
                    margin: 20px 30px 20px 30px;
                    }
                </style>
                <title>Recept</title>
            </head>
            <body>
                <div style="padding:30px;">
                    <p style="margin-bottom: 70px;">Obr. LR-1</p>
                    <p align="center" style="margin-bottom:-20px;"><xsl:value-of select="//zko:prezime"/>, <xsl:value-of select="//zko:ime"/></p>
                    <p align="center">____________________________________________________________</p>
                    <p align="center" style="margin-top:-20px">Prezime i ime osiguranog lica</p>
                    <span><xsl:value-of select="//zko:datum_rodjenja"/></span>
                    <span>.        <xsl:value-of select="recept:osnova_oslobadjenja_participacije"/> </span><br/>
                    <span>_______________________________________                  _______________</span>
                    <p><xsl:value-of select="//zko:zdravstveni_karton/@broj_zdr_knjizice"/></p>
                    <p>__________________________________________________________________</p>
                    <div align="center" style="text-align: center;">
                        <p><xsl:value-of select="//recept:datum"/> </p>
                        <span>________________________________</span><br/>
                        <p><xsl:value-of select="//zko:zdravstveni_karton/@broj_kartona"/></p>
                        <span>________________________________</span><br/>
                    </div>
                </div>


            </body>
        </html>

    </xsl:template>

</xsl:stylesheet>