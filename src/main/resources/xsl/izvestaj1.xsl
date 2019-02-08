<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:izvestaj="http://www.zis.rs/seme/izvestaj"
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
                    margin: 10px 20px 10px 20px;
                    }
                    p {
                    margin: 0px 0px 0px 0px;

                    }


                </style>
                <title > <b>Izveštaj lekara</b></title><br/>
            </head>
            <body>


                <table wisth="400" height="800">
                    <col width="50%" height="100%"/>
                    <col width="50%" height="100%"/>
                    <tr>
                        <td>
                            <b> Prezime i ime pacijenta: </b>
                        </td>
                        <td>
                            <xsl:value-of select="//izvestaj:osigurano_lice//zko:zdravstveni_karton//zko:osigurano_lice//zko:prezime"/>
                            <span style="color:white">-</span><xsl:value-of select="//izvestaj:osigurano_lice//zko:zdravstveni_karton//zko:osigurano_lice//zko:ime"/>

                        </td>
                    </tr>
                    <tr><br></br></tr>
                    <tr>
                        <td>
                            <b>JMBG:</b>
                        </td>
                        <td>
                            <xsl:value-of select="//izvestaj:osigurano_lice//zko:zdravstveni_karton/@jmbg"/>
                        </td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td>
                            <b>Datum rodjenja:</b>
                        </td>
                        <td>
                            <xsl:value-of select="//izvestaj:osigurano_lice//zko:zdravstveni_karton//zko:osigurano_lice//zko:datum_rodjenja"/>

                        </td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td>
                            <b>Adresa:</b>
                        </td>
                        <td>
                            <xsl:value-of select="//izvestaj:osigurano_lice//zko:zdravstveni_karton//zko:adresa//zko:ulica"/>
                            <span style="color:white">-</span>
                            <xsl:value-of select="//izvestaj:osigurano_lice//zko:zdravstveni_karton//zko:adresa//zko:broj" />
                            <span> , </span>
                            <xsl:value-of select="//izvestaj:osigurano_lice//zko:zdravstveni_karton//zko:adresa//zko:postanski_broj" />
                            <span> , </span>
                            <xsl:value-of select="//izvestaj:osigurano_lice//zko:zdravstveni_karton//zko:adresa//zko:mesto" />
                            <span> , </span>
                            <xsl:value-of select="//izvestaj:osigurano_lice//zko:zdravstveni_karton//zko:adresa//zko:opstina" />

                        </td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td>
                            <b>Anamneza:</b>
                        </td>
                        <td>
                            <xsl:value-of select="//izvestaj:anamneza"/>
                        </td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td>
                            <b>Dijagnoza:</b>
                        </td>
                        <td>
                            <xsl:value-of select="//izvestaj:dijagnoza"/>
                        </td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td>
                            <b>Terapija:</b>
                        </td>
                        <td>
                            <xsl:value-of select="//izvestaj:terapija"/>
                        </td>
                    </tr>
                    <tr></tr>
                </table>

                <br></br>
                <br></br>
                <br></br>

                <table>
                    <tr>
                        <td><xsl:value-of select="//izvestaj:datum"/></td>
                    </tr>
                    <tr>
                        <td>__________________________<span style="color:white">--------------------</span></td>
                        <td>__________________________ <span style="color:white">--------------------</span></td>
                        <td>__________________________ <span style="color:white">--------------------</span></td>
                    </tr>
                    <tr>
                        <td>Datum</td>
                        <td>M.P</td>
                        <td>Lekar dr. <xsl-valueOf select="//izvestaj//lek:"></xsl-valueOf></td>
                    </tr>
                </table>




            </body>

        </html>
    </xsl:template>


</xsl:stylesheet>