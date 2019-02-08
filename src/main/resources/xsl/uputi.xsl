<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:uput="http://www.zis.rs/seme/uput"
                xmlns:uputi="http://www.zis.rs/seme/uputi"
                xmlns:zko="http://www.zis.rs/seme/zdravstveni_karton"
                xmlns:lkr="http://www.zis.rs/seme/lekar">

    <xsl:template match="/">
        <html>
            <head>
                <style>
                    div {
                        margin: 20px 30px 20px 30px;

                    }
                    p {
                        margin: 0px 0px 0px 0px;
                    }
                </style>
            </head>
            <body>
                <p align="center"><b>UPUT ZA AMBULANTNO-SPECIJALISTICKI PREGLED
                    <span style="color: white">-----------------</span> Obrazac OZ-2</b></p><br/><br/><br/>
                <p>Broj zdravstvenog kartona: <span style="color:DimGray"><xsl:value-of select="//zko:zdravstveni_karton/@broj_kartona"/></span>
                    <span style="color:white">------------</span>Lekaru specijalisti za: <span style="color:DimGray">
                    <xsl:value-of select="//uput:specialista/lkr:lekar/lkr:tip"/></span></p><br/><br/>
                <hr/>
                <div>
                    <p>Upucuje se <span style="color:white">--------------------------------</span><span style="color:DimGray"><xsl:value-of select="//zko:prezime"/><span style="color:white">-</span>
                    <xsl:value-of select="//zko:ime"/></span></p>
                    <p><span style="color:white">-----------------------------------------------</span>(prezime i ime)</p><br/><br/>
                    <p>JMBG <span style="color:white">---</span><span style="color:DimGray"><xsl:value-of select="//zko:zdravstveni_karton/@jmbg"/>
                    </span><span style="color:white">------------------------------</span> LBO <span style="color:white">----</span>
                    <span style="color:DimGray"><xsl:value-of select="//zko:zdravstveni_karton/@lbo"/></span></p><br/><br/>
                    <p>clan je porodice <span style="color:white">-------------</span><span style="color:DimGray"><xsl:value-of
                            select="//zko:nosilac_osiguranja/zko:srodstvo"/></span><span style="color:white">-------------------</span>
                        <span style="color:DimGray"><xsl:value-of select="//zko:nosilac_osiguranja/zko:prezime"/><span style="color:white">-</span>
                            <xsl:value-of select="//zko:nosilac_osiguranja/zko:ime"/></span></p>
                    <p><span style="color:white">---------------------------------</span>srodstvo<span style="color:white">-------------------</span>
                    {prezime i ime nosioca osiguranja</p><br/><br/>
                    <p><span style="color:white">---------------------------------------</span><span style="color:DimGray">
                        <xsl:value-of select="//zko:nosilac_osiguranja/zko:osnova_osiguranja"/></span></p>
                    <p><span style="color:white">----------------------------------</span>(osnov osiguranja)</p><br/><br/>
                    <p>Upucuje se na specijalisticki pregled radi: <span style="color:DimGray"><xsl:value-of select="//uput:misljenje"/></span> </p>
                    <br/><br/><br/><br/>
                    <p><span style="color:white">------------</span>20 god.<span style="color:white">--------------</span>M.P.</p>
                    <p><span style="color:white">------------------------------------------------------</span>(potpis i faksimil doktora medicine)</p>
                    <hr/>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>