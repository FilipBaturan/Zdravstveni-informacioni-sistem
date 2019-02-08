<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:recepti="http://www.zis.rs/seme/recepti"
                xmlns:recept="http://www.zis.rs/seme/recept"
                xmlns:zko="http://www.zis.rs/seme/zdravstveni_karton"
                xmlns:lkr="http://www.zis.rs/seme/lekar">

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
                    p {
                    margin: 0px 0px 0px 0px;
                    }
                </style>
                <title>Recept</title>
            </head>
            <body>
                <div style="padding:30px;">
                    <p style="margin-bottom: 60px;">Obr. LR-1</p>

                    <p style="color:DimGray" align="center"><xsl:value-of select="//zko:prezime"/>
                        <span style="color:white">-</span><xsl:value-of select="//zko:ime"/></p>
                    <p align="center">____________________________________________________________</p>
                    <p align="center">Prezime i ime osiguranog lica</p><br/>
                    <br/>
                    <p style="color:DimGray"><span style="color:white">----------------------</span><xsl:value-of select="//zko:datum_rodjenja"/>
                        <span style="color:white">------------------------------------------</span>
                        <xsl:value-of select="//recept:osnova_oslobadjenja_participacije"/></p>
                    <span>_______________________________________</span>
                    <span style="color:white">______</span>
                    <span style="right:0px">______________________</span><br/>

                    <p><span style="color:white">----------</span>Datum rodjenja osiguranog lica
                        <span style="color:white">---------------------</span>
                        Osnov oslobadjanja participacije
                    </p>
                    <p style="color:DimGray"><span style="color:white">-------------------</span>
                        <xsl:value-of select="//zko:zdravstveni_karton/@broj_zdr_knjizice"/></p>
                    <p>_________________________________________</p>
                    <p><span style="color:white">------------</span>Broj zdravstvene knjizice</p><br/><br/>
                    <div align="center" style="text-align: center;">
                        <p style="color:DimGray"><xsl:value-of select="//recept:datum"/></p>
                        <span>________________________________</span><br/>
                        <p>Datum propisivanja leka</p>
                        <p style="color:DimGray"><xsl:value-of select="//zko:zdravstveni_karton/@broj_kartona"/> </p>
                        <span>________________________________</span><br/>
                        <p>Broj kartona</p>
                        <p style="color:DimGray"><xsl:value-of select="//lkr:lekar/@id"/></p>
                        <span>_________________________________</span><br/>
                        <p>ID broj lekara</p><br/>
                    </div><br/>
                    <br/><br/>
                    <p><b>Rp.</b></p><br/><br/><br/>
                    <p style="color:DimGray"><span style="color:white">---------------------------------------------------
                            -------------------------------------</span><xsl:value-of select="//recept:dijagnoza"/></p>
                    <p>_________________________________<span style="color:white">--------------------------------</span>________________</p>
                    <p><span style="color:white">-----------</span>Sifra propisanog leka
                        <span style="color:white">------------------------------------------------------</span>Dijagnoza</p>
                    <p style="color:DimGray"><span style="color:white">------------</span><xsl:value-of select="//recept:datum"/></p>
                    <p>_________________________________<span style="color:white">--------</span>_______________________________</p>
                    <p><span style="color:white">---------------</span>Datum izdavanja leka
                        <span style="color:white">--------------------------------</span>Sifra izdatog leka</p>
                </div>


            </body>
        </html>

    </xsl:template>

</xsl:stylesheet>